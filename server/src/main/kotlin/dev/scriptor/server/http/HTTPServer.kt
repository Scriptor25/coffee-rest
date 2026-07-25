package dev.scriptor.server.http

import dev.scriptor.server.*
import dev.scriptor.server.address.AddressType.*
import dev.scriptor.server.address.normalizeIpv4
import dev.scriptor.server.address.normalizeIpv6
import dev.scriptor.server.address.normalizeName
import dev.scriptor.server.address.parseAddressType
import dev.scriptor.server.annotation.*
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.type.isAssignable
import java.io.IOException
import java.lang.AutoCloseable
import java.lang.reflect.InvocationTargetException
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.*
import java.util.logging.Logger
import kotlin.concurrent.timerTask
import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
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

    private val routes = mutableMapOf<HTTPMethod, MutableList<Route>>()
    private val converters = mutableListOf<ConversionStep>()
    private val contexts = mutableMapOf<String, Any>()
    private val injected = mutableMapOf<String, Any?>()

    private val timer = Timer()
    private val tasks = mutableMapOf<String, Task>()

    private val queue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        10,
        10,
        100,
        TimeUnit.MILLISECONDS,
        queue
    )

    private val conversionCache = mutableMapOf<Pair<KType, KType>, List<ConversionStep>>()

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

        val name = converter.name
        if (name != null) {
            inject(name, converter)
        }
    }

    fun registerContext(name: String, instance: Any) {
        contexts[name] = instance

        injectInstance(instance)

        inject(name, instance)
    }

    fun registerEndpoint(instance: Any) {
        val name = instance::class.qualifiedName
        if (name != null) {
            contexts[name] = instance
        }

        injectInstance(instance)
    }

    fun inject(name: String, value: Any?) {
        injected[name] = value

        for ((_, instance) in contexts) {
            for (property in instance::class.memberProperties) {
                if (property !is KMutableProperty<*>) continue

                val inject = property.findAnnotation<Inject>() ?: continue
                if (inject.value != name) continue

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

    private fun injectInstance(instance: Any) {
        for (property in instance::class.memberProperties) {
            if (property !is KMutableProperty<*>) continue

            val inject = property.findAnnotation<Inject>() ?: continue
            if (inject.value !in injected) continue

            property.setter.call(instance, injected[inject.value])
        }
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

        findConversionPath(src, dst)
            ?: return false

        return true
    }

    private fun checkConvertible(src: KType, dst: KType) {
        if (convertible(src, dst)) return

        throw Exception("unsupported conversion from '$src' to '$dst'")
    }

    private fun <D> convert(value: Any, src: KType, dst: KType): D {

        if (isAssignable(dst, src)) {
            return value as D
        }

        val path = findConversionPath(src, dst)
            ?: throw Exception("unsupported conversion from '$src' to '$dst'")

        var current = value

        for ((_, _, converter) in path) {
            current = converter.convert(current)
        }

        return current as D
    }

    private fun process(channel: SocketChannel) {
        val reader = HTTPRequestMessageReader(BufferedReadableByteChannel(channel))

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

            var contentType = "*/*"

            var result: HTTPResult<*>
            try {
                if (opt.isEmpty) {
                    throw NotFoundSignal()
                }

                val bundle = opt.get()

                contentType = bundle.result

                val parameters = bundle.callee.parameters
                val arguments = arrayOfNulls<Any>(parameters.size)

                for (i in parameters.indices) {
                    if (i == 0) {
                        arguments[i] = bundle.instance
                        continue
                    }

                    val parameter = parameters[i]

                    val typename: String
                    val value: Any?
                    when {
                        parameter.hasAnnotation<PathParameter>() -> {
                            val name = parameter.findAnnotation<PathParameter>()!!.value

                            typename = "path $name"
                            value = bundle.route.get(request.path, name)
                        }

                        parameter.hasAnnotation<QueryParameter>() -> {
                            val name = parameter.findAnnotation<QueryParameter>()!!.value
                            val values = request.query.getAll(name)

                            typename = "query $name"
                            value =
                                if (parameter.type.classifier == Array::class)
                                    values.toTypedArray()
                                else
                                    values.firstOrNull()
                        }

                        parameter.hasAnnotation<Header>() -> {
                            val name = parameter.findAnnotation<Header>()!!.value
                            val values = request.headers.getAll(name)

                            typename = "header $name"
                            value =
                                if (parameter.type.classifier == Array::class)
                                    values.toTypedArray()
                                else
                                    values.firstOrNull()
                        }

                        parameter.hasAnnotation<Body>() -> {
                            typename = "body"

                            value = request.body
                        }

                        else -> continue
                    }

                    if (value != null) {
                        val type =
                            if (value::class == Array<String>::class)
                                typeOf<Array<String>>()
                            else
                                value::class.starProjectedType

                        try {
                            arguments[i] = convert(
                                value,
                                type,
                                parameter.type,
                            )
                        } catch (_: Exception) {
                            throw BadRequestSignal(content = "failed to convert parameter '$typename'")
                        }
                    } else if (!parameter.isOptional && !parameter.type.isMarkedNullable) {
                        throw BadRequestSignal(content = "parameter '$typename' is neither optional nor nullable")
                    }
                }

                val value: Any?
                val type = bundle.callee.returnType

                try {
                    value = bundle.callee.call(*arguments)
                } catch (e: InvocationTargetException) {
                    throw e.targetException
                }

                if (value == null) {
                    throw NotFoundSignal()
                }

                result = convert(
                    value,
                    type,
                    typeOf<HTTPResult<*>>(),
                )
            } catch (s: Signal) {
                result = s.generate()
            } catch (t: Throwable) {
                result = InternalServerErrorSignal().generate()

                log.trace(t)
            }

            val headers = ParameterList(result.headers)

            if ("content-type" !in headers) {
                headers["content-type"] = contentType
            }

            var chunked = false
            if ("content-length" !in headers && "transfer-encoding" !in headers) {
                chunked = result.count < 0

                if (chunked) {
                    headers["transfer-encoding"] = "chunked"
                } else {
                    headers["content-length"] = result.count.toString()
                }
            }

            HTTPResponseMessage(
                "HTTP/1.1",
                result.statusCode,
                result.statusText,
                headers,
                if (result.channel != null)
                    HTTPMessageBody(
                        result.channel,
                        result.position,
                        result.count,
                        chunked,
                    )
                else null,
            ).use { it.write(channel) }
        } while (keepAlive)
    }
}
