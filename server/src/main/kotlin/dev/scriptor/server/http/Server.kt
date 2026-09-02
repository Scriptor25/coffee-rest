package dev.scriptor.server.http

import dev.scriptor.reflect.*
import dev.scriptor.server.*
import dev.scriptor.server.converter.ConverterFn
import dev.scriptor.server.result.Result
import java.io.IOException
import java.lang.AutoCloseable
import java.lang.reflect.InvocationTargetException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.SeekableByteChannel
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.file.Path
import java.util.*
import java.util.concurrent.*
import java.util.logging.Logger
import kotlin.concurrent.timerTask
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.measureTime

class Server : AutoCloseable {

    val log: Logger
    val provider: Provider

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

    constructor(
        log: Logger,
        provider: Provider = Provider(),
        port: Int,
    ) : this(
        log,
        provider,
        InetSocketAddress(port),
    )

    constructor(
        log: Logger,
        provider: Provider = Provider(),
        addr: InetAddress?,
        port: Int,
    ) : this(
        log,
        provider,
        InetSocketAddress(addr, port),
    )

    constructor(
        log: Logger,
        provider: Provider = Provider(),
        hostname: String,
        port: Int,
    ) : this(
        log,
        provider,
        InetSocketAddress(hostname, port),
    )

    constructor(
        log: Logger,
        provider: Provider = Provider(),
        local: SocketAddress? = null,
    ) {
        this.log = log
        this.provider = provider

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
        parameters: List<Parameter> = emptyList(),
        returns: Type = getType<Unit>(),
        callee: (Map<Int, Any?>) -> Any?,
    ) {
        val metadata = RouteMetadata(
            method,
            Pathname(path),
            accept,
            result,
            parameters,
            returns,
            callee,
        )

        routes.computeIfAbsent(method) { mutableListOf() } += metadata
    }

    fun check() {
        for ((_, entries) in routes) {
            for (entry in entries) {
                checkConvertible(
                    entry.returns,
                    getType<Result>(),
                )
            }
        }
    }

    fun register(name: String, delay: Duration, period: Duration, callee: Server.() -> Unit) {
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
        parameters: List<Parameter>,
        arguments: MutableMap<Int, Any?>,
    ) {
        for (parameter in parameters) {
            when (parameter.kind) {
                ParameterKind.CONTEXT -> {
                    arguments[parameter.index] = when (parameter.type) {
                        is ClassReference -> when (parameter.type.id) {
                            getClassId<Logger>() -> log
                            getClassId<Provider>() -> provider
                            getClassId<ConverterFn<*, *>>() -> {
                                val src = parameter.type.arguments[0]
                                val dst = parameter.type.arguments[1]

                                val srcType = if (src is TypeProjection) src.type else error("star projection")
                                val dstType = if (dst is TypeProjection) dst.type else error("star projection")

                                provider[srcType to dstType]
                            }

                            else -> provider[parameter.type]
                        }

                        else -> provider[parameter.type]
                    }
                }

                ParameterKind.VALUE -> {
                    val typename: String
                    val contains: Boolean
                    val type: Type
                    val value: Any?

                    when (val annotation = parameter.annotation) {
                        is ParameterAnnotation.Path -> {
                            val name = annotation.name ?: parameter.name
                            val values = route.pathname[path, name]

                            typename = "path $name"
                            value =
                                if (parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()) {
                                    contains = true
                                    type = getType<Array<String>>()
                                    values.toTypedArray()
                                } else {
                                    val value = values.firstOrNull()

                                    contains = parameter.type.nullable || value != null
                                    type = getType<String>()
                                    value
                                }
                        }

                        is ParameterAnnotation.Query -> {
                            val name = annotation.name ?: parameter.name
                            val values = request.query.getAll(name)

                            typename = "query $name"
                            value =
                                if (parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()) {
                                    contains = true
                                    type = getType<Array<String>>()
                                    values.toTypedArray()
                                } else {
                                    contains = parameter.type.nullable || name in request.query
                                    type = getType<String>()
                                    values.firstOrNull()
                                }
                        }

                        is ParameterAnnotation.Header -> {
                            val name = annotation.name ?: parameter.name
                            val values = request.headers.getAll(name)

                            typename = "header $name"
                            value =
                                if (parameter.type is ClassReference && parameter.type.id == getClassId<Array<*>>()) {
                                    contains = true
                                    type = getType<Array<String>>()
                                    values.toTypedArray()
                                } else {
                                    contains = parameter.type.nullable || name in request.headers
                                    type = getType<String>()
                                    values.firstOrNull()
                                }
                        }

                        is ParameterAnnotation.Body -> {
                            typename = "body"
                            contains = true
                            type = getType<MessageBody>()
                            value = request.body
                        }

                        else -> error("$parameter is missing annotation")
                    }

                    if (!contains) {
                        if (parameter.optional) continue
                        throw BadRequestSignal(content = "parameter '$typename' is not optional")
                    }

                    if (value == null) {
                        if (parameter.type.nullable) {
                            arguments[parameter.index] = null
                            continue
                        }
                        throw BadRequestSignal(content = "parameter '$typename' is not nullable")
                    }

                    try {
                        arguments[parameter.index] = convert(
                            value,
                            type,
                            parameter.type,
                        )
                    } catch (e: Exception) {
                        log.severe(e.stackTraceToString())
                        throw BadRequestSignal(content = "failed to convert parameter '$typename'")
                    }
                }
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

            return Result(
                result.statusCode,
                result.statusText,
                result.contentLength,
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

        if ("access-control-allow-origin" !in headers) {
            headers["access-control-allow-origin"] = "*"
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

            if (
                "content-length" !in headers
                && "transfer-encoding" !in headers
                && result.contentLength
                && request.method != Method.HEAD
            ) {
                headers["content-length"] = "0"
            }
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
