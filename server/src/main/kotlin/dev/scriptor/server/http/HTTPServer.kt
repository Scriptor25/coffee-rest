package dev.scriptor.server.http

import dev.scriptor.server.address.AddressType.*
import dev.scriptor.server.address.normalizeIpv4
import dev.scriptor.server.address.normalizeIpv6
import dev.scriptor.server.address.normalizeName
import dev.scriptor.server.address.parseAddressType
import dev.scriptor.server.annotation.*
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultUnit
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
import kotlin.concurrent.timerTask
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

    private data class ConversionStep(
        val src: KType,
        val dst: KType,
        val converter: Converter<Any, Any>,
    )

    private data class Task(
        val interval: Long,
        val callee: Runnable,
        val task: TimerTask,
    )

    private val server = ServerSocketChannel.open()

    private val routes: MutableMap<HTTPMethod, MutableList<Route>> = EnumMap(HTTPMethod::class.java)
    private val converters: MutableList<ConversionStep> = mutableListOf()
    private val instances: MutableList<Any> = mutableListOf()
    private val context: MutableMap<String, Any?> = mutableMapOf()

    private val timer = Timer()
    private val tasks: MutableMap<String, Task> = mutableMapOf()

    private val queue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        10,
        10,
        100,
        TimeUnit.MILLISECONDS,
        queue
    )

    private val conversionCache: MutableMap<Pair<KType, KType>, List<ConversionStep>> = HashMap()

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

        log.info("server listening on ${if (':' in normalized) "[$normalized]" else normalized}:$port")
    }

    override fun close() {
        server.close()
        timer.cancel()
    }

    fun registerRoute(
        instance: Any,
        callee: KCallable<*>,
        endpoint: Endpoint,
        resource: Resource,
    ): Route {
        checkConvertible(callee.returnType, typeOf<HTTPResult<*>>())

        val route = Route(
            instance,
            callee,
            HTTPRoute(endpoint.value, resource.path),
            resource.method,
            resource.accept,
            resource.result
        )

        routes.computeIfAbsent(resource.method) { mutableListOf() } += route

        return route
    }

    fun registerConverter(
        src: KType,
        dst: KType,
        converter: Converter<*, *>,
    ) {
        converters += ConversionStep(
            src,
            dst,
            converter as Converter<Any, Any>,
        )
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

    fun registerTask(name: String, interval: Long, callee: Runnable) {
        val task = timerTask { callee.run() }
        tasks[name] = Task(interval, callee, task)

        timer.scheduleAtFixedRate(task, interval, interval)
    }

    fun cancelTask(name: String) {
        val value = tasks.remove(name) ?: return
        value.task.cancel()
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

    private fun findConversionPath(src: KType, dst: KType): List<ConversionStep>? {

        val key = Pair(src, dst)

        if (key in conversionCache) {
            return conversionCache[key]
        }

        data class Node(
            val type: KType,
            val path: List<ConversionStep>,
        )

        val queue = ArrayDeque<Node>()
        val visited = HashSet<KType>()

        queue.add(Node(src, emptyList()))

        while (queue.isNotEmpty()) {

            val current = queue.removeFirst()

            if (!visited.add(current.type)) continue

            if (isAssignable(dst, current.type)) {
                conversionCache[key] = current.path
                return current.path
            }

            val edges = converters.filter { isAssignable(it.src, current.type) }

            for ((_, next, converter) in edges) {
                queue += Node(
                    next,
                    current.path + ConversionStep(
                        current.type,
                        next,
                        converter,
                    )
                )
            }
        }

        return null
    }

    private fun convertible(src: KType, dst: KType): Boolean {

        if (isAssignable(dst, src)) {
            return true
        }

        findConversionPath(
            src.withNullability(false),
            dst.withNullability(false),
        ) ?: return false

        return true
    }

    private fun checkConvertible(src: KType, dst: KType) {
        if (convertible(src, dst)) return

        throw Exception("unsupported conversion from '$src' to '$dst'")
    }

    private fun <D> convert(value: Any?, src: KType, dst: KType): D? {

        if (value == null) {
            return null
        }

        if (isAssignable(dst, src)) {
            return value as D
        }

        val path = findConversionPath(
            src.withNullability(false),
            dst.withNullability(false),
        ) ?: throw Exception("unsupported conversion from '$src' to '$dst'")

        var current: Any = value

        for ((_, _, converter) in path) {
            current = converter.convert(current)
        }

        return current as D
    }

    private fun process(channel: SocketChannel) {
        val reader = HTTPRequestMessageReader(channel)

        var keepAlive: Boolean
        do {
            val request = reader.read() ?: break

            log.info("${request.method} ${request.path} ${request.protocol}")

            keepAlive = request.headers["connection"]?.lowercase() != "close"

            val opt = routes
                .computeIfAbsent(request.method) { mutableListOf() }
                .stream()
                .filter { it.route.matches(request.path) }
                .max(Comparator.naturalOrder())

            if (opt.isEmpty) {
                notFound().use { it.write(channel) }
                continue
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
                        val values = request.headers.getAll(name.lowercase())
                        value =
                            if (parameter.type.classifier == Array::class)
                                values.toTypedArray()
                            else
                                values.firstOrNull()
                    } else if (parameter.hasAnnotation<PathParameter>()) {
                        val name = parameter.findAnnotation<PathParameter>()!!.value
                        value = bundle.route.get(request.path, name.lowercase())
                    } else if (parameter.hasAnnotation<QueryParameter>()) {
                        val name = parameter.findAnnotation<QueryParameter>()!!.value
                        val values = request.query.getAll(name.lowercase())
                        value =
                            if (parameter.type.classifier == Array::class)
                                values.toTypedArray()
                            else
                                values.firstOrNull()
                    } else {
                        value = null
                    }

                    if (value != null) {
                        val type =
                            if (value::class == Array<String>::class)
                                typeOf<Array<String>>()
                            else
                                value::class.starProjectedType

                        val c = convert<Any?>(
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
                    result = HTTPResultUnit()
                } else {
                    val c = convert<HTTPResult<*>>(
                        value,
                        type,
                        typeOf<HTTPResult<*>>(),
                    ) ?: throw Exception("failed to convert '$value' from '$type' to HTTP result")

                    result = c
                }
            } catch (e: Exception) {
                log.trace(e)

                internalServerError(e.stackTraceToString()).use { it.write(channel) }
                continue
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
            ).use { it.write(channel) }
        } while (keepAlive)
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
