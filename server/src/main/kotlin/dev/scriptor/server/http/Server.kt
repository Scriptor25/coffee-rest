package dev.scriptor.server.http

import dev.scriptor.server.*
import dev.scriptor.server.address.AddressType.*
import dev.scriptor.server.address.normalizeIpv4
import dev.scriptor.server.address.normalizeIpv6
import dev.scriptor.server.address.normalizeName
import dev.scriptor.server.address.parseAddressType
import dev.scriptor.server.annotation.*
import dev.scriptor.server.converter.ConversionPath
import dev.scriptor.server.result.Result
import dev.scriptor.server.result.UnitResult
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
import kotlin.reflect.KParameter
import kotlin.reflect.KParameter.Kind.*
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.typeOf

class Server(
    val log: Logger,
    val provider: Provider = Provider(),
    val hostname: String = "0.0.0.0",
    val port: Int = 8080,
) : AutoCloseable {

    private val server = ServerSocketChannel.open()

    private val routes = mutableMapOf<Method, MutableList<Route>>()

    private val timer = Timer()
    private val tasks = mutableMapOf<String, TimerTask>()

    private val queue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        10,
        10,
        100,
        TimeUnit.MILLISECONDS,
        queue
    )

    private var running: Boolean = false

    init {
        val addressType = parseAddressType(hostname)
        val normalized = when (addressType) {
            INVALID -> throw Error("invalid hostname '$hostname'")
            IPV4 -> normalizeIpv4(hostname)
            IPV6 -> normalizeIpv6(hostname)
            NAME -> normalizeName(hostname)
        }

        server.bind(InetSocketAddress(normalized, port))

        log.info("server listening on ${if (':' in normalized) "[$normalized]" else normalized}:$port")
    }

    override fun close() {
        server.close()
        timer.cancel()
    }

    fun register(
        instance: Any,
        callee: KCallable<*>,
        endpoint: String,
        resource: Resource,
    ) {
        val route = Route(
            instance,
            callee,
            Pathname(endpoint, resource.path),
            resource.method,
            resource.accept,
            resource.result
        )

        routes.computeIfAbsent(resource.method) { mutableListOf() } += route
    }

    fun check() {
        for ((_, entries) in routes) {
            for ((_, callee) in entries) {
                checkConvertible(callee.returnType, typeOf<Result>())
            }
        }
    }

    fun register(name: String, delay: Long, period: Long, callee: Runnable) {
        val task = timerTask { callee.run() }
        tasks[name] = task
        timer.scheduleAtFixedRate(task, delay, period)
    }

    fun cancel(name: String) {
        val task = tasks.remove(name) ?: return
        task.cancel()
    }

    fun spin() {
        val socket = server.accept()

        executor.execute {
            try {
                handle(socket)
            } catch (e: IOException) {
                log.severe(e.stackTraceToString())
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

    private fun checkConvertible(src: KType, dst: KType) {
        if (src to dst in provider) return

        throw Exception("unsupported conversion from '$src' to '$dst'")
    }

    private fun convert(value: Any, src: KType, dst: KType): Any {

        val path = provider[src to dst]
            ?: throw Exception("unsupported conversion from '$src' to '$dst'")

        return context(provider) { path.convert(value) }
    }

    private fun handle(channel: SocketChannel) {
        val reader = RequestReader(BufferedReadableByteChannel(channel))

        do while (handle(channel, reader.read() ?: break))
    }

    private fun getOptions(request: Request): Result {
        val headers = ParameterList()

        headers["access-control-allow-origin"] = "*"

        headers["access-control-allow-methods"] =
            routes
                .filter { (_, value) -> value.any { it.pathname.matches(request.path) } }
                .map { it.key }
                .plusElement(Method.OPTIONS)
                .joinToString(", ")

        request.headers["access-control-request-headers"]?.let {
            headers["access-control-allow-headers"] = it
        }

        headers["access-control-max-age"] = "3600"

        return UnitResult(
            204,
            "No Content",
            headers,
        )
    }

    private fun getArguments(
        request: Request,
        route: Route,
        parameters: List<KParameter>,
        arguments: Array<Any?>,
    ) {
        for ((index, parameter) in parameters.withIndex()) {
            when (parameter.kind) {
                INSTANCE -> {
                    arguments[index] = route.instance
                }

                @OptIn(ExperimentalContextParameters::class)
                CONTEXT -> {
                    arguments[index] = when (parameter.type.classifier) {
                        Logger::class -> log
                        Provider::class -> provider
                        ConversionPath::class -> {
                            val src = parameter.type.arguments[0].type!!
                            val dst = parameter.type.arguments[1].type!!

                            provider[src to dst]
                        }

                        else -> provider[parameter.type]
                    }
                }

                VALUE -> {
                    val typename: String
                    val value: Any?

                    when {
                        parameter.hasAnnotation<PathParameter>() -> {
                            val name = parameter.findAnnotation<PathParameter>()!!.value.ifEmpty { parameter.name!! }

                            typename = "path $name"
                            value = route.pathname.get(request.path, name)
                        }

                        parameter.hasAnnotation<QueryParameter>() -> {
                            val name = parameter.findAnnotation<QueryParameter>()!!.value.ifEmpty { parameter.name!! }
                            val values = request.query.getAll(name)

                            typename = "query $name"
                            value =
                                if (parameter.type.classifier == Array::class)
                                    values.toTypedArray()
                                else
                                    values.firstOrNull()
                        }

                        parameter.hasAnnotation<Header>() -> {
                            val name = parameter.findAnnotation<Header>()!!.value.ifEmpty { parameter.name!! }
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

                        else -> throw UnsupportedOperationException()
                    }

                    if (value == null) {
                        if (parameter.isOptional || parameter.type.isMarkedNullable) continue

                        throw BadRequestSignal(content = "parameter '$typename' is neither optional nor nullable")
                    }

                    val type =
                        if (value::class == Array<String>::class)
                            typeOf<Array<String>>()
                        else
                            value::class.starProjectedType

                    try {
                        arguments[index] = convert(
                            value,
                            type,
                            parameter.type,
                        )
                    } catch (e: Exception) {
                        log.severe(e.stackTraceToString())
                        throw BadRequestSignal(content = "failed to convert parameter '$typename'")
                    }
                }

                EXTENSION_RECEIVER -> throw UnsupportedOperationException()
            }
        }
    }

    private fun getResult(request: Request): Result {

        val candidate = routes
            .computeIfAbsent(request.method) { mutableListOf() }
            .stream()
            .filter { it.pathname.matches(request.path) }
            .max(Comparator.naturalOrder())

        if (candidate.isEmpty) {
            return NotFoundSignal().generate()
        }

        val route = candidate.get()
        val parameters = route.callee.parameters
        val arguments = arrayOfNulls<Any>(parameters.size)

        try {
            getArguments(
                request,
                route,
                parameters,
                arguments,
            )

            val value: Any?
            val type = route.callee.returnType

            try {
                value = route.callee.call(*arguments)
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }

            if (value == null) {
                throw UnsupportedOperationException()
            }

            val result = convert(
                value,
                type,
                typeOf<Result>(),
            ) as Result

            return Result(
                result.statusCode,
                result.statusText,
                if (route.result == "*/*") result.contentType else route.result,
                result.headers,
                result.channel,
                result.position,
                result.count,
            )
        } catch (s: Signal) {
            return s.generate()
        } catch (t: Throwable) {
            log.severe(t.stackTraceToString())
            return InternalServerErrorSignal().generate()
        }
    }

    private fun handle(channel: SocketChannel, request: Request): Boolean {
        log.info("${request.method} ${request.path} ${request.protocol}")

        val keepAlive = request.headers["connection"]?.lowercase() != "close"

        val result = when (request.method) {
            Method.OPTIONS -> getOptions(request)
            else -> getResult(request)
        }

        val headers = ParameterList(result.headers)
        val body: MessageBody?

        if (result.channel != null) {
            if ("content-type" !in headers) {
                headers["content-type"] = result.contentType
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

            body = MessageBody(
                result.channel,
                result.position,
                result.count,
                chunked,
            )
        } else {
            body = null
        }

        if ("access-control-allow-origin" !in headers) {
            headers["access-control-allow-origin"] = "*"
        }

        val response = Response(
            "HTTP/1.1",
            result.statusCode,
            result.statusText,
            headers,
            body,
        )

        ResponseWriter(channel).write(response)

        body?.close()

        return keepAlive
    }
}
