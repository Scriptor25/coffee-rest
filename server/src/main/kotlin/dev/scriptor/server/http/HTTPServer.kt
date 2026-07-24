package dev.scriptor.server.http

import dev.scriptor.server.address.AddressType.*
import dev.scriptor.server.address.normalizeIpv4
import dev.scriptor.server.address.normalizeIpv6
import dev.scriptor.server.address.normalizeName
import dev.scriptor.server.address.parseAddressType
import dev.scriptor.server.annotation.*
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultVoid
import dev.scriptor.server.trace
import dev.scriptor.server.type.isAssignable
import java.io.IOException
import java.lang.AutoCloseable
import java.net.InetSocketAddress
import java.nio.channels.Channels
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.*
import java.util.logging.Logger
import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

class HTTPServer(
    val log: Logger,
    val hostname: String,
    val port: Int,
) : AutoCloseable {

    data class Route(
        val instance: Any,
        val callee: KCallable<*>,
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

    private val server = ServerSocketChannel.open()

    private val routes: MutableMap<HTTPMethod, MutableList<Route>> = EnumMap(HTTPMethod::class.java)
    private val converters: MutableMap<KType, MutableMap<KType, Converter<*, *>>> = HashMap()

    private val queue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        10,
        10,
        100,
        TimeUnit.MILLISECONDS,
        queue
    )

    private val instances: MutableList<Any> = ArrayList()
    private val context: MutableMap<String, Any?> = HashMap()

    private var running: Boolean = false

    init {
        server.bind(InetSocketAddress(hostname, port))

        val addressType = parseAddressType(hostname)
        val normalized = when (addressType) {
            INVALID -> throw Error("invalid hostname '$hostname'")
            IPV4 -> normalizeIpv4(hostname)
            IPV6 -> normalizeIpv6(hostname)
            NAME -> normalizeName(hostname)
        }

        log.info("server listening on http://${if (':' in normalized) "[$normalized]" else normalized}:$port")
    }

    fun registerRoute(
        instance: Any,
        callee: KCallable<*>,
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

    fun <S : Any, D : Any> registerConverter(
        source: KType,
        destination: KType,
        converter: Converter<S, D>,
    ) {
        converters.computeIfAbsent(source) { HashMap() }[destination] = converter
    }

    fun registerInstance(instance: Any) {
        instances.add(instance)

        for (property in instance::class.memberProperties) {
            if (property !is KMutableProperty<*>) continue

            val inject = property.findAnnotation<Inject>() ?: continue
            if (inject.value !in context) continue

            property.setter.call(instance, context[inject.value])
        }
    }

    fun inject(key: String, value: Any?) {
        context[key] = value

        for (instance in instances) {
            for (property in instance::class.memberProperties) {
                if (property !is KMutableProperty<*>) continue

                val inject = property.findAnnotation<Inject>() ?: continue
                if (inject.value != key) continue

                property.setter.call(instance, value)
            }
        }
    }

    fun spin() {
        val socket = server.accept()

        executor.execute {
            try {
                process(socket)
            } catch (e: IOException) {
                log.trace(e)
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

    private fun <S, D> convert(value: S, source: KType, destination: KType): D? {

        if (isAssignable(destination, source)) {
            return value as D?
        }

        val source = source.withNullability(false)
        val destination = destination.withNullability(false)

        if (source in converters && destination in converters[source]!!) {
            return (converters[source]!![destination]!! as Converter<S, D>).from(value)
        }

        throw IllegalStateException("unsupported conversion from '$source' to '$destination'")
    }

    private fun process(channel: SocketChannel) {
        val request = HTTPRequestMessage.read(channel)
        if (request == null) {
            log.warning("invalid request: request is null")
            return
        }

        log.info("${request.method} ${request.path} ${request.protocol}")

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

        val parameters = bundle.callee.parameters
        val arguments = arrayOfNulls<Any>(parameters.size)

        val result: HTTPResult<*>
        try {
            for (i in parameters.indices) {
                if (i == 0) {
                    arguments[i] = bundle.instance
                    continue
                }

                val parameter = parameters[i]

                val value: Any?
                if (parameter.hasAnnotation<Body>()) {
                    value = request.body
                } else if (parameter.hasAnnotation<Header>()) {
                    val name = parameter.findAnnotation<Header>()!!.value
                    value = request.headers[name.lowercase()]
                } else if (parameter.hasAnnotation<PathParameter>()) {
                    val name = parameter.findAnnotation<PathParameter>()!!.value
                    value = bundle.route.get(request.path, name.lowercase())
                } else if (parameter.hasAnnotation<QueryParameter>()) {
                    val name = parameter.findAnnotation<QueryParameter>()!!.value
                    val values = request.query[name.lowercase()]
                    value =
                        if (parameter.type.classifier == Array::class)
                            values?.toTypedArray() ?: emptyArray<String>()
                        else
                            values?.firstOrNull()
                } else {
                    value = null
                }

                if (value != null) {
                    val type =
                        if (value::class == Array<String>::class)
                            typeOf<Array<String>>()
                        else
                            value::class.starProjectedType

                    val c = convert<Any?, Any?>(
                        value,
                        type,
                        parameter.type,
                    )

                    arguments[i] = c
                }
            }

            val value = bundle.callee.call(*arguments)
            val type = bundle.callee.returnType

            if (type.classifier == Unit::class) {
                result = HTTPResultVoid()
            } else {
                val c = convert<Any?, HTTPResult<*>>(
                    value,
                    type,
                    typeOf<HTTPResult<*>>(),
                ) ?: throw Exception("failed to convert '$value' from '$type' to HTTP result")

                result = c
            }
        } catch (e: Exception) {
            log.trace(e)

            internalServerError(e.stackTraceToString()).write(channel).close()
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
