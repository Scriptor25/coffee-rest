package dev.scriptor.server.http

import dev.scriptor.server.annotation.*
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.log.info
import dev.scriptor.server.log.trace
import dev.scriptor.server.log.warning
import dev.scriptor.server.type.IConverter
import dev.scriptor.server.type.normalize
import java.io.IOException
import java.lang.AutoCloseable
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.Type
import java.net.InetSocketAddress
import java.nio.channels.Channels
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.*
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class HTTPServer(port: Int) : AutoCloseable {

    data class Route(
        val instance: Any,
        val callee: Method,
        val route: HTTPRoute,
        val method: HTTPMethod,
        val accept: String,
        val result: String
    ) : Comparable<Route> {

        override fun compareTo(other: Route): Int {
            return this.route.compareTo(other.route)
        }

        override fun toString(): String {
            return "$method $route : $accept -> $result"
        }
    }

    private val server: ServerSocketChannel

    private val routes: MutableMap<HTTPMethod, MutableList<Route>> = EnumMap(HTTPMethod::class.java)
    private val converters: MutableMap<Type, MutableMap<Type, IConverter<*, *>>> = HashMap()

    private val workQueue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        10,
        10,
        100,
        TimeUnit.MILLISECONDS,
        workQueue
    )

    private var running: Boolean = false

    init {
        server = ServerSocketChannel.open()
        server.bind(InetSocketAddress("0.0.0.0", port))

        info("server listening on port $port")
    }

    fun registerRoute(
        instance: Any,
        callee: Method,
        endpoint: Endpoint,
        resource: Resource
    ): Route {
        val route = Route(
            instance,
            callee,
            HTTPRoute(endpoint.value, resource.path),
            resource.method,
            resource.accept,
            resource.result
        )

        routes.computeIfAbsent(resource.method) { ArrayList() }.add(route)

        return route
    }

    fun <S, D> registerConverter(
        source: Type,
        destination: Type,
        converter: IConverter<S, D>
    ) {
        converters.computeIfAbsent(source) { HashMap() }[destination] = converter
    }

    fun spin() {
        val socket = server.accept()

        executor.execute {
            try {
                process(socket)
            } catch (e: IOException) {
                trace(e)
            } finally {
                socket.close()
            }
        }
    }

    fun start() {
        running = true

        while (running && !Thread.interrupted()) {
            spin()
        }
    }

    fun stop() {
        running = false
    }

    private fun <S, D> convert(obj: S, source: Type, destination: Type): D? {
        val source = source.normalize()
        val destination = destination.normalize()

        if (IConverter.isAssignable(destination, source)) {
            return obj as D?
        }

        if (source in converters && destination in converters[source]!!) {
            return (converters[source]!![destination]!! as IConverter<S, D>).from(obj)
        }

        throw IllegalStateException("unsupported conversion from '$source' to '$destination'")
    }

    private fun process(channel: SocketChannel) {
        val request = HTTPRequestMessage.read(channel)
        if (request == null) {
            warning("invalid request: request is null")
            return
        }

        info("${request.method} ${request.path} ${request.protocol}")

        val opt = routes
            .computeIfAbsent(request.method) { ArrayList() }
            .stream()
            .filter { x -> x.route.matches(request.path) }
            .max(Comparator { obj, other -> obj.compareTo(other) })

        if (opt.isEmpty) {
            notFound().write(channel).close()
            return
        }

        val bundle = opt.get()

        val parameterCount = bundle.callee.parameterCount
        val parameters = bundle.callee.parameters

        val args = arrayOfNulls<Any>(parameterCount)

        val result: HTTPResult<*>
        try {
            for (i in 0 until parameterCount) {
                val parameter: Parameter = parameters[i]

                val value: Any?
                if (parameter.isAnnotationPresent(Body::class.java)) {
                    value = request.body
                } else if (parameter.isAnnotationPresent(Header::class.java)) {
                    val name = parameter.getAnnotation(Header::class.java).value
                    value = request.headers[name.lowercase()]
                } else if (parameter.isAnnotationPresent(PathParameter::class.java)) {
                    val name = parameter.getAnnotation(PathParameter::class.java).value
                    value = bundle.route.get(request.path, name.lowercase())
                } else if (parameter.isAnnotationPresent(QueryParameter::class.java)) {
                    val name = parameter.getAnnotation(QueryParameter::class.java).value
                    val values = request.query[name.lowercase()]
                    value =
                        if (parameter.type.isArray) values?.toTypedArray() ?: arrayOf<String>()
                        else values?.firstOrNull()
                } else {
                    value = null
                }

                if (value != null) {
                    val c = convert<Any, Any>(
                        value,
                        value.javaClass,
                        parameter.type
                    )
                    if (c == null) {
                        throw Exception("failed to convert '$value' from '${value.javaClass}' to '${parameter.type}'")
                    }
                    args[i] = c
                }
            }

            val obj = bundle.callee.invoke(bundle.instance, *args)
            val type = bundle.callee.genericReturnType

            @OptIn(ExperimentalStdlibApi::class)
            val c = convert<Any?, HTTPResult<*>>(obj, type, typeOf<HTTPResult<*>>().javaType)
                ?: throw Exception("failed to convert '$obj' from '$type' to HTTP result")

            result = c
        } catch (e: Exception) {
            trace(e)

            internalServerError(e.message).write(channel).close()
            return
        }

        val headers: MutableMap<String, String> = HashMap(result.headers)
        headers.computeIfAbsent("Content-Type") { bundle.result }

        val chunked: Boolean
        if ("Content-Length" !in headers && "Transfer-Encoding" !in headers) {
            chunked = result.count < 0

            if (chunked) {
                headers["Transfer-Encoding"] = "chunked"
            } else {
                headers["Content-Length"] = result.count.toString()
            }
        } else {
            chunked = false
        }

        HTTPResponseMessage(
            "HTTP/1.1",
            result.statusCode,
            result.statusText,
            headers,
            chunked,
            result.position,
            result.count,
            result.channel,
        ).write(channel).close()
    }

    override fun close() {
        server.close()
    }

    private fun notFound(): HTTPResponseMessage {
        val bytes = "resource not found".encodeToByteArray()
        val input = bytes.inputStream()
        val body = Channels.newChannel(input)

        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "text/plain"
        headers["Content-Length"] = bytes.size.toString()

        return HTTPResponseMessage(
            "HTTP/1.1",
            404,
            "Not Found",
            headers,
            false,
            0L,
            bytes.size.toLong(),
            body,
        )
    }

    private fun internalServerError(message: String?): HTTPResponseMessage {
        val bytes = "internal server error: $message".encodeToByteArray()
        val input = bytes.inputStream()
        val body = Channels.newChannel(input)

        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "text/plain"
        headers["Content-Length"] = bytes.size.toString()

        return HTTPResponseMessage(
            "HTTP/1.1",
            500,
            "Internal Server Error",
            headers,
            false,
            0L,
            bytes.size.toLong(),
            body,
        )
    }
}
