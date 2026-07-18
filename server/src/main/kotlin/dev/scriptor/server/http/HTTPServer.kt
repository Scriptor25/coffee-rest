package dev.scriptor.server.http

import dev.scriptor.server.annotation.*
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.log.info
import dev.scriptor.server.log.trace
import dev.scriptor.server.log.warning
import dev.scriptor.server.type.IConverter
import dev.scriptor.server.type.normalize
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.AutoCloseable
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.Type
import java.net.ServerSocket
import java.net.Socket
import java.security.KeyStore
import java.util.*
import java.util.concurrent.*
import javax.net.ServerSocketFactory
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class HTTPServer(
    port: Int,
    private val tls: Boolean,
    keystoreFilename: String?,
    keystorePassphrase: String?
) : AutoCloseable {

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
            return "%s %s : %s -> %s".format(method, route, accept, result)
        }
    }

    private val serverSocket: ServerSocket

    private val routes: MutableMap<HTTPMethod, MutableList<Route>> = EnumMap(HTTPMethod::class.java)
    private val converters: MutableMap<Type, MutableMap<Type, IConverter<*, *>>> = HashMap()

    private val workQueue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        10,
        10,
        10,
        TimeUnit.MINUTES,
        workQueue
    )

    private var running: Boolean = false

    init {
        val serverSocketFactory: ServerSocketFactory
        if (tls) {
            val passphrase = keystorePassphrase!!.toCharArray()
            val keystore = KeyStore.getInstance("JKS")
            keystore.load(FileInputStream(keystoreFilename!!), passphrase)

            val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
            keyManagerFactory.init(keystore, passphrase)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(keyManagerFactory.keyManagers, null, null)

            serverSocketFactory = sslContext.serverSocketFactory
        } else {
            serverSocketFactory = ServerSocketFactory.getDefault()
        }

        serverSocket = serverSocketFactory.createServerSocket(port)
        info("HTTP%s server listening on port %d", if (tls) "S" else "", port)
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

    fun handleRequest() {
        val socket = serverSocket.accept()
        executor.execute {
            try {
                if (tls) {
                    (socket as SSLSocket).startHandshake()
                }
                handleRequest(socket)
            } catch (e: IOException) {
                trace(e)
            }
        }
    }

    fun start() {
        running = true

        while (running && !Thread.interrupted()) {
            handleRequest()
        }
    }

    fun stop() {
        running = false
    }

    private fun <S, D> convert(obj: S, source: Type, destination: Type): D {
        val source = source.normalize()
        val destination = destination.normalize()

        if (IConverter.isAssignable(destination, source)) {
            return obj as D
        }

        if (source in converters && destination in converters[source]!!) {
            return (converters[source]!![destination]!! as IConverter<S, D>).from(obj)
        }

        throw IllegalStateException("unsupported conversion from '%s' to '%s'".format(source, destination))
    }

    private fun handleRequest(socket: Socket) {
        socket.getInputStream().use { inputStream ->
            socket.getOutputStream().use { outputStream ->
                val request = HTTPRequestMessage.read(inputStream)
                if (request == null) {
                    warning("invalid request: request is null")
                    return
                }

                info("%s %s %s", request.method, request.path, request.protocol)

                val opt = routes
                    .computeIfAbsent(request.method) { ArrayList() }
                    .stream()
                    .filter { x -> x.route.matches(request.path) }
                    .max(Comparator { obj, other -> obj.compareTo(other) })

                if (opt.isEmpty) {
                    notFound().write(outputStream)
                    return
                }

                val bundle = opt.get()

                val parameterCount = bundle.callee.parameterCount
                val parameters = bundle.callee.parameters

                val args = arrayOfNulls<Any>(parameterCount)

                val result: HTTPResult<*>
                try {
                    for (i in 0..<parameterCount) {
                        val parameter: Parameter = parameters[i]

                        val value: Any?
                        if (parameter.isAnnotationPresent(Body::class.java)) {
                            value = request.body
                        } else if (parameter.isAnnotationPresent(Header::class.java)) {
                            val name = parameter.getAnnotation(Header::class.java).value
                            value = request.headers[name.lowercase(Locale.getDefault())]
                        } else if (parameter.isAnnotationPresent(Path::class.java)) {
                            val name = parameter.getAnnotation(Path::class.java).value
                            value = bundle.route.get(request.path, name)
                        } else if (parameter.isAnnotationPresent(Query::class.java)) {
                            val name = parameter.getAnnotation(Query::class.java).value
                            val values = request.query.computeIfAbsent(name) { ArrayList() }
                            value =
                                if (parameter.type.isArray) values.toTypedArray()
                                else values.firstOrNull()
                        } else {
                            value = null
                        }

                        if (value != null) {
                            args[i] = convert(
                                value,
                                value.javaClass,
                                parameter.type
                            )
                        }
                    }

                    val obj = bundle.callee.invoke(bundle.instance, *args)
                    val type = bundle.callee.genericReturnType

                    @OptIn(ExperimentalStdlibApi::class)
                    result = convert(obj, type, typeOf<HTTPResult<*>>().javaType)
                } catch (e: Exception) {
                    trace(e)
                    HTTPResponseMessage(
                        "HTTP/1.1",
                        500,
                        "Internal Server Error - %s".format(e.message),
                        HashMap(),
                        null,
                        false
                    ).write(outputStream)
                    return
                }

                val headers: MutableMap<String, String> = HashMap(result.headers)
                headers["Content-Type"] = bundle.result
                headers["Connection"] = "Close"

                val chunked = result.size < 0
                if (chunked) {
                    headers["Transfer-Encoding"] = "chunked"
                } else {
                    headers["Content-Length"] = result.size.toString()
                }

                HTTPResponseMessage(
                    "HTTP/1.1",
                    result.statusCode,
                    result.statusText,
                    headers,
                    result.stream,
                    chunked
                ).write(outputStream)
            }
        }
    }

    override fun close() {
        serverSocket.close()
    }

    companion object {
        private fun notFound(): HTTPResponseMessage {
            val bytes = "resource not found".toByteArray()

            val headers: MutableMap<String, String> = HashMap()
            headers["Content-Type"] = "text/plain"
            headers["Content-Length"] = bytes.size.toString()
            headers["Connection"] = "Close"

            val body: InputStream = ByteArrayInputStream(bytes)

            return HTTPResponseMessage(
                "HTTP/1.1",
                404,
                "Not Found",
                headers,
                body,
                false
            )
        }
    }
}
