package dev.scriptor.server.http

import dev.scriptor.reflect.Type
import dev.scriptor.reflect.getType
import dev.scriptor.server.*
import dev.scriptor.server.address.AddressType.*
import dev.scriptor.server.address.normalizeIpv4
import dev.scriptor.server.address.normalizeIpv6
import dev.scriptor.server.address.normalizeName
import dev.scriptor.server.address.parseAddressType
import dev.scriptor.server.annotation.*
import dev.scriptor.server.converter.ConverterFn
import dev.scriptor.server.result.Result
import java.io.IOException
import java.lang.AutoCloseable
import java.lang.reflect.InvocationTargetException
import java.net.InetSocketAddress
import java.nio.channels.SeekableByteChannel
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.*
import java.util.logging.Logger
import kotlin.concurrent.timerTask
import kotlin.io.path.Path
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.KParameter.Kind.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.typeOf
import kotlin.time.Clock
import kotlin.time.measureTime

class Server(
    val log: Logger,
    val provider: Provider = Provider(),
    val hostname: String = "0.0.0.0",
    val port: Int = 8080,
) : AutoCloseable {

    private val server = ServerSocketChannel.open()

    private val routes = mutableMapOf<Method, MutableList<RouteMetadata>>()

    private val timer = Timer()
    private val tasks = mutableMapOf<String, TimerTask>()

    private val queue: BlockingQueue<Runnable> = ArrayBlockingQueue(256)
    private val executor: Executor = ThreadPoolExecutor(
        16,
        256,
        100,
        TimeUnit.MILLISECONDS,
        queue
    )

    private var next = 0L
    private val nextHeap = mutableSetOf<Long>()

    private var running: Boolean = false

    init {
        val addressType = parseAddressType(hostname)
        val normalized = when (addressType) {
            INVALID -> error("invalid hostname '$hostname'")
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
        instance: Any?,
        callee: KCallable<*>,
        base: String,
        route: Route,
    ) {
        val metadata = RouteMetadata(
            instance,
            callee,
            Pathname(Path(base, route.path)),
            route.method,
            route.accept.ifEmpty { null },
            route.result.ifEmpty { null },
        )

        routes.computeIfAbsent(route.method) { mutableListOf() } += metadata
    }

    fun check() {
        for ((_, entries) in routes) {
            for ((_, callee) in entries) {
                checkConvertible(getType(callee.returnType), getType<Result>())
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

    private fun checkConvertible(src: Type, dst: Type) {
        if (src to dst in provider) return

        error("unsupported conversion from $src to $dst")
    }

    private fun convert(value: Any?, src: Type, dst: Type): Any? {

        val convert = provider[src to dst]
            ?: error("unsupported conversion from $src to $dst")

        return context(provider) { convert(value) }
    }

    private fun handle(channel: SocketChannel) {
        val reader = RequestReader(BufferedReadableByteChannel(channel))

        val id = if (nextHeap.isNotEmpty()) {
            val key = nextHeap.min()
            nextHeap.remove(key)
            key
        } else next++

        log.fine("#$id connect")

        var alive = true
        while (alive) {
            val request = reader.read() ?: break

            log.info("#$id $request")

            val delta = measureTime { alive = handle(channel, request) }

            log.fine("#$id $delta (${if (alive) "keep-alive" else "close"})")
        }

        log.fine("#$id disconnect")
        nextHeap.add(id)
    }

    private fun getOptions(request: Request): Result {

        val methods = when (val target = request.target) {
            is OriginRequestTarget -> routes
                .filterValues { values -> values.any { target.path in it.pathname } }
                .map { it.key }
                .toSet()

            is AsteriskRequestTarget -> routes
                .map { it.key }
                .toSet()

            else -> error("unsupported request target '$target'")
        }

        val headers = ParameterList(
            "access-control-allow-origin" to "*",
            "access-control-allow-methods" to (methods + Method.HEAD + Method.OPTIONS).joinToString(", "),
            "access-control-allow-headers" to (request.headers["access-control-request-headers"] ?: "*"),
            "access-control-max-age" to "3600",
        )

        return NoContentSignal(headers).generate()
    }

    private fun getHeaders(request: Request): Result {

        val candidates = routes
            .computeIfAbsent(Method.GET) { mutableListOf() }
            .filter {
                when (val target = request.target) {
                    is OriginRequestTarget -> target.path in it.pathname
                    else -> false
                }
            }

        val route = candidates.maxOrNull()
            ?: return NotFoundSignal().generate()

        val headers = ParameterList()

        if (route.accept != null) {
            headers["accept"] = route.accept
        }

        if (route.result != null) {
            headers["content-type"] = route.result
        }

        return NoContentSignal(headers).generate()
    }

    private fun getArguments(
        request: Request,
        path: String,
        route: RouteMetadata,
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
                        ConverterFn::class -> {
                            val src = parameter.type.arguments[0].type!!
                            val dst = parameter.type.arguments[1].type!!

                            provider[getType(src) to getType(dst)]
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
                            value = route.pathname[path, name]
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

                        else -> error("$parameter is missing annotation")
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
                            getType(type),
                            getType(parameter.type),
                        )
                    } catch (e: Exception) {
                        log.severe(e.stackTraceToString())
                        throw BadRequestSignal(content = "failed to convert parameter '$typename'")
                    }
                }

                EXTENSION_RECEIVER -> error("$parameter not supported")
            }
        }
    }

    private fun getResult(request: Request): Result {

        val candidates = routes
            .computeIfAbsent(request.method) { mutableListOf() }
            .filter {
                when (val target = request.target) {
                    is OriginRequestTarget -> target.path in it.pathname
                    else -> false
                }
            }

        if (request.method == Method.HEAD && candidates.isEmpty()) {
            return getHeaders(request)
        }

        val route = candidates.maxOrNull()
            ?: return NotFoundSignal().generate()

        val parameters = route.callee.parameters
        val arguments = arrayOfNulls<Any>(parameters.size)

        val path = when (val target = request.target) {
            is OriginRequestTarget -> target.path
            else -> "/"
        }

        try {
            getArguments(
                request,
                path,
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

            val result = convert(
                value,
                getType(type),
                getType<Result>(),
            ) as Result

            return Result(
                result.statusCode,
                result.statusText,
                route.result ?: result.contentType,
                result.headers,
                result.channel,
            )
        } catch (s: Signal) {
            return s.generate()
        } catch (t: Throwable) {
            log.severe(t.stackTraceToString())
            return InternalServerErrorSignal().generate()
        }
    }

    private fun handle(channel: SocketChannel, request: Request): Boolean {
        val connection = request.headers["connection"]?.lowercase()
        val keepAlive = when (request.protocol) {
            Version.HTTP_0_9 -> false
            Version.HTTP_1_0 -> connection == "keep-alive"
            Version.HTTP_1_1 -> connection != "close"
        }

        val result = when (request.method) {
            Method.CONNECT -> MethodNotAllowedSignal().generate()
            Method.OPTIONS -> getOptions(request)
            else -> getResult(request)
        }

        val headers = ParameterList(result.headers)
        val body: MessageBody?

        if ("date" !in headers) {
            val now = Clock.System.now()
            headers["date"] = now.toHTTP()
        }

        if ("server" !in headers) {
            headers["server"] = "coffee-rest/1.0.0"
        }

        if (result.channel != null) {
            if ("content-type" !in headers) {
                headers["content-type"] = result.contentType ?: "*/*"
            }

            val length = when (val c = result.channel) {
                is SeekableByteChannel -> c.size() - c.position()
                is RangeReadableByteChannel -> c.remaining
                else -> -1L
            }

            val chunked = if ("transfer-encoding" in headers) {
                headers["transfer-encoding"] == "chunked"
            } else {
                length < 0L
            }

            if ("content-length" !in headers && "transfer-encoding" !in headers) {
                if (chunked) {
                    headers["transfer-encoding"] = "chunked"
                } else {
                    headers["content-length"] = length.toString()
                }
            }

            body = MessageBody(result.channel, chunked)
        } else {
            body = null
        }

        val response = Response(
            request.protocol,
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
