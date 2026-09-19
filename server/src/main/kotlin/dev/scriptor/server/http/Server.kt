package dev.scriptor.server.http

import dev.scriptor.reflect.*
import dev.scriptor.server.*
import dev.scriptor.server.converter.ConverterFn
import dev.scriptor.server.request.AsteriskRequestTarget
import dev.scriptor.server.request.OriginRequestTarget
import dev.scriptor.server.request.Request
import dev.scriptor.server.request.RequestReader
import dev.scriptor.server.response.Response
import dev.scriptor.server.response.ResponseWriter
import dev.scriptor.server.result.Result
import dev.scriptor.server.security.*
import java.io.IOException
import java.lang.AutoCloseable
import java.lang.reflect.InvocationTargetException
import java.net.SocketAddress
import java.nio.channels.SeekableByteChannel
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.file.Path
import java.util.*
import java.util.concurrent.*
import java.util.logging.Logger
import kotlin.concurrent.timerTask
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.reflect.KClass
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.measureTime

class Server(
    val log: Logger,
    val provider: Provider,
    val authenticator: Authenticator?,
    val authorizer: Authorizer,
    local: SocketAddress?,
) : AutoCloseable {

    private val server = ServerSocketChannel.open()

    private val routes = mutableMapOf<Method, MutableList<RouteMetadata>>()
    private val handlers = mutableMapOf<KClass<out Signal>, MutableMap<Path, SignalHandler<*>>>()

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
    private val nextHeap = ConcurrentHashMap<Long, Unit>()

    private var running: Boolean = false

    init {
        server.bind(local)

        log.info("server listening on http:/${server.localAddress}")
    }

    override fun close() {
        server.close()
        timer.cancel()
    }

    fun register(
        method: Method = Method.GET,
        path: Path,
        accept: String? = null,
        result: String? = null,
        security: SecurityPolicy = SecurityPolicy.Public,
        parameters: List<Parameter> = emptyList(),
        returns: Type = getType<Unit>(),
        callee: (Map<Int, Any?>) -> Any?,
    ) {
        val metadata = RouteMetadata(
            method,
            Pathname(path),
            accept,
            result,
            security,
            parameters,
            returns,
            callee,
        )

        routes.computeIfAbsent(method) { mutableListOf() } += metadata
    }

    fun check() {
        for ((_, entries) in routes) {
            for (entry in entries) {
                for (parameter in entry.parameters) {
                    val hasValue = when (parameter.kind) {
                        ParameterKind.CONTEXT -> when (parameter.type) {
                            is ClassReference -> when (parameter.type.id) {
                                getClassId<Logger>() -> true
                                getClassId<Provider>() -> true
                                getClassId<ConverterFn<*, *>>() -> {
                                    val src = parameter.type.arguments[0]
                                    val dst = parameter.type.arguments[1]

                                    val srcType =
                                        if (src is TypeProjection) src.type
                                        else error("converting star projection not supported (at $parameter)")
                                    val dstType =
                                        if (dst is TypeProjection) dst.type
                                        else error("converting star projection not supported (at $parameter)")

                                    (srcType to dstType) in provider
                                }

                                else -> parameter.type in provider
                            }

                            else -> parameter.type in provider
                        }

                        ParameterKind.VALUE -> {
                            val hasValue: Boolean
                            val type: Type

                            when (val annotation = parameter.annotation) {
                                is ParameterAnnotation.Path -> {
                                    val name = annotation.name ?: parameter.name
                                    val collecting = entry.pathname.collecting(name)

                                    hasValue = name in entry.pathname
                                    type =
                                        if (collecting) getType<Array<String>>()
                                        else getType<String>()
                                }

                                is ParameterAnnotation.Query -> {
                                    val collecting =
                                        parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()

                                    hasValue = true
                                    type =
                                        if (collecting) getType<Array<String>>()
                                        else getType<String>()
                                }

                                is ParameterAnnotation.Header -> {
                                    val collecting =
                                        parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()

                                    hasValue = true
                                    type =
                                        if (collecting) getType<Array<String>>()
                                        else getType<String>()
                                }

                                is ParameterAnnotation.Body -> {
                                    hasValue = true
                                    type = getType<MessageBody>()
                                }

                                is ParameterAnnotation.None -> error("$parameter is missing annotation")
                            }

                            if (hasValue) {
                                checkConvertible(
                                    type,
                                    parameter.type,
                                    "at $entry -> parameter $parameter",
                                )
                            }

                            hasValue
                        }
                    }

                    if (!hasValue) {
                        if (parameter.optional) continue
                        error("no value for non-optional parameter $parameter")
                    }
                }

                checkConvertible(
                    entry.returns,
                    getType<Result>(),
                    "at $entry -> return type"
                )
            }
        }
    }

    fun <S : Signal> register(signal: KClass<S>, handler: SignalHandler<S>) {
        handlers.computeIfAbsent(signal) { mutableMapOf(Path("/") to handler) }
    }

    inline fun <reified S : Signal> register(handler: SignalHandler<S>) {
        register(S::class, handler)
    }

    fun <S : Signal> register(base: String, signal: KClass<S>, handler: SignalHandler<S>) {
        handlers.computeIfAbsent(signal) { mutableMapOf(Path(base) to handler) }
    }

    inline fun <reified S : Signal> register(base: String, handler: SignalHandler<S>) {
        register(base, S::class, handler)
    }

    fun <S : Signal> register(base: Path, signal: KClass<S>, handler: SignalHandler<S>) {
        handlers.computeIfAbsent(signal) { mutableMapOf(base to handler) }
    }

    inline fun <reified S : Signal> register(base: Path, handler: SignalHandler<S>) {
        register(base, S::class, handler)
    }

    fun register(
        name: String,
        delay: Duration,
        period: Duration,
        callee: Server.() -> Unit,
    ) {
        val task = timerTask { callee() }

        tasks[name] = task

        timer.scheduleAtFixedRate(
            task,
            delay.inWholeMilliseconds,
            period.inWholeMilliseconds,
        )
    }

    fun cancel(name: String) {
        val task = tasks.remove(name)
            ?: return

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

    private fun checkConvertible(src: Type, dst: Type, where: String) {
        if ((src to dst) in provider) return

        error("no conversion path from $src to $dst ($where)")
    }

    private fun convert(value: Any?, src: Type, dst: Type): Any? {
        val convert = provider[src to dst]
            ?: error("no conversion path from $src to $dst")

        return context(provider) { convert(value) }
    }

    private fun handle(channel: SocketChannel) {
        val reader = RequestReader(BufferedReadableByteChannel(channel))

        val id = when (val key = nextHeap.keys.minOrNull()) {
            null -> next++
            else -> {
                nextHeap.remove(key)
                key
            }
        }

        log.fine("#$id connect")

        var alive = true
        while (alive) {
            val request = reader.read() ?: break

            log.info("#$id $request")

            val delta = measureTime { alive = handle(channel, request) }

            log.fine("#$id $delta (${if (alive) "keep-alive" else "close"})")
        }

        log.fine("#$id disconnect")
        nextHeap[id] = Unit
    }

    private fun withPrincipal(request: Request, route: RouteMetadata, block: (Principal?) -> Result): Result {
        val principal = authenticator?.authenticate(request)

        when (authorizer.authorize(principal, route.security)) {
            AuthorizationResult.Allowed -> Unit

            AuthorizationResult.Unauthenticated ->
                return UnauthorizedSignal().generate()

            AuthorizationResult.Forbidden ->
                return ForbiddenSignal().generate()
        }

        return block(principal)
    }

    private fun getOptions(request: Request): Result {

        val methods = when (val target = request.target) {
            is OriginRequestTarget -> routes
                .filterValues { values -> values.any { it.pathname.matches(target.path) } }
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
                    is OriginRequestTarget -> it.pathname.matches(target.path)
                    else -> false
                }
            }

        val route = candidates.maxOrNull()
            ?: return NotFoundSignal().generate()

        return withPrincipal(request, route) {
            val headers = ParameterList()

            if (route.accept != null) {
                headers["accept"] = route.accept
            }

            if (route.result != null) {
                headers["content-type"] = route.result
            }

            NoContentSignal(headers).generate()
        }
    }

    private fun getContextArgument(parameter: Parameter, principal: Principal?): Pair<Boolean, Any?> {
        return when (parameter.type) {
            is ClassReference -> when (parameter.type.id) {
                getClassId<Logger>() -> true to log
                getClassId<Provider>() -> true to provider
                getClassId<Principal>() -> true to principal
                getClassId<ConverterFn<*, *>>() -> {
                    val src = parameter.type.arguments[0]
                    val dst = parameter.type.arguments[1]

                    val srcType =
                        if (src is TypeProjection) src.type
                        else error("converting star projection not supported (at $parameter)")
                    val dstType =
                        if (dst is TypeProjection) dst.type
                        else error("converting star projection not supported (at $parameter)")

                    val key = srcType to dstType

                    (key in provider) to (provider[key])
                }

                else -> (parameter.type in provider) to (provider[parameter.type])
            }

            else -> (parameter.type in provider) to (provider[parameter.type])
        }
    }

    private fun getValueParameter(
        request: Request,
        path: String,
        route: RouteMetadata,
        parameter: Parameter,
    ): Pair<Boolean, Any?> {
        val type: Type
        val hasValueOrNull: Boolean
        val value: Any?

        when (val annotation = parameter.annotation) {
            is ParameterAnnotation.Path -> {
                val name = annotation.name ?: parameter.name

                val collecting = route.pathname.collecting(name)
                val values = route.pathname[path, name]

                hasValueOrNull = name in route.pathname
                value =
                    if (collecting) {
                        type = getType<Array<String>>()
                        values.toTypedArray()
                    } else {
                        type = getType<String>()
                        values.firstOrNull()
                    }
            }

            is ParameterAnnotation.Query -> {
                val name = annotation.name ?: parameter.name

                val collecting = parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()
                val values = request.query.getAll(name)

                hasValueOrNull = parameter.type.nullable || name in request.query
                value =
                    if (collecting) {
                        type = getType<Array<String>>()
                        values.toTypedArray()
                    } else {
                        type = getType<String>()
                        values.firstOrNull()
                    }
            }

            is ParameterAnnotation.Header -> {
                val name = annotation.name ?: parameter.name

                val collecting = parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()
                val values = request.headers.getAll(name)

                hasValueOrNull = parameter.type.nullable || name in request.headers
                value =
                    if (collecting) {
                        type = getType<Array<String>>()
                        values.toTypedArray()
                    } else {
                        type = getType<String>()
                        values.firstOrNull()
                    }
            }

            is ParameterAnnotation.Body -> {
                type = getType<MessageBody>()
                hasValueOrNull = true
                value = request.body
            }

            is ParameterAnnotation.None -> error("$parameter is missing annotation")
        }

        if (!hasValueOrNull) {
            return false to null
        }

        if (value == null) {
            return true to null
        }

        return true to convert(value, type, parameter.type)
    }

    private fun getArgument(
        request: Request,
        path: String,
        route: RouteMetadata,
        parameter: Parameter,
        principal: Principal?,
    ): Pair<Boolean, Any?> {
        return when (parameter.kind) {
            ParameterKind.CONTEXT -> getContextArgument(parameter, principal)
            ParameterKind.VALUE -> getValueParameter(request, path, route, parameter)
        }
    }

    private fun getArguments(
        request: Request,
        path: String,
        route: RouteMetadata,
        parameters: List<Parameter>,
        arguments: MutableMap<Int, Any?>,
        principal: Principal?,
    ) {
        for (parameter in parameters) {
            val (hasValue, value) = getArgument(request, path, route, parameter, principal)

            if (!hasValue) {
                if (parameter.optional) continue
                throw BadRequestSignal()
            }

            if (value == null && !parameter.type.nullable) {
                throw BadRequestSignal()
            }

            arguments[parameter.index] = value
        }
    }

    private fun getResult(request: Request): Result {

        val candidates = routes
            .computeIfAbsent(request.method) { mutableListOf() }
            .filter {
                when (val target = request.target) {
                    is OriginRequestTarget -> it.pathname.matches(target.path)
                    else -> false
                }
            }

        if (request.method == Method.HEAD && candidates.isEmpty()) {
            return getHeaders(request)
        }

        val route = candidates.maxOrNull()
            ?: return NotFoundSignal().generate()

        return withPrincipal(request, route) { principal ->
            val parameters = route.parameters
            val arguments = mutableMapOf<Int, Any?>()

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
                    principal,
                )

                val value: Any?
                val type = route.returns

                try {
                    value = route.callee(arguments)
                } catch (e: InvocationTargetException) {
                    throw e.targetException
                }

                val result = convert(
                    value,
                    type,
                    getType<Result>(),
                ) as Result

                Result(
                    result.statusCode,
                    result.statusText,
                    result.contentLength,
                    route.result ?: result.contentType,
                    result.headers,
                    result.channel,
                )
            } catch (s: Signal) {
                s.generate()
            } catch (t: Throwable) {
                log.severe(t.stackTraceToString())
                InternalServerErrorSignal().generate()
            }
        }
    }

    private fun handleSignal(request: Request, result: Result): Result {
        val signal = Signal.of(result)
        val mapping = handlers[signal::class]
            ?: return result

        when (request.target) {
            is OriginRequestTarget -> {
                val targetPath = Path(request.target.path).absolute()

                var bestCount: Int = -1
                var best: SignalHandler<*>? = null

                for ((path, handler) in mapping) {
                    if (targetPath.startsWith(path)) {
                        if (path.nameCount > bestCount) {
                            bestCount = path.nameCount
                            best = handler
                        }
                    }
                }

                if (best != null) {
                    return (best as SignalHandler<Signal>).handle(request, signal)
                }
            }

            else -> Unit
        }

        return result
    }

    private fun handle(channel: SocketChannel, request: Request): Boolean {
        val connection = request.headers["connection"]?.lowercase()
        val keepAlive = when (request.protocol) {
            Version.HTTP_0_9 -> false
            Version.HTTP_1_0 -> connection == "keep-alive"
            Version.HTTP_1_1 -> connection != "close"
        }

        var result = when (request.method) {
            Method.CONNECT -> MethodNotAllowedSignal().generate()
            Method.OPTIONS -> getOptions(request)
            else -> getResult(request)
        }

        result = handleSignal(request, result)

        val headers = ParameterList(result.headers)

        if ("date" !in headers) {
            val now = Clock.System.now()
            headers["date"] = now.toHTTP()
        }

        if ("server" !in headers) {
            headers["server"] = "coffee-rest/1.0.0"
        }

        if ("access-control-allow-origin" !in headers) {
            headers["access-control-allow-origin"] = "*"
        }

        val body: MessageBody? = if (result.channel != null) {
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

            MessageBody(result.channel, chunked)
        } else {
            if (
                "content-length" !in headers
                && "transfer-encoding" !in headers
                && result.contentLength
                && request.method != Method.HEAD
            ) {
                headers["content-length"] = "0"
            }

            null
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
