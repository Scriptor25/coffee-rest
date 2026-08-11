package dev.scriptor.server.http

import dev.scriptor.computeIfAbsent
import dev.scriptor.io.Path
import dev.scriptor.io.channels.BufferedReadableByteChannel
import dev.scriptor.io.channels.ServerSocketChannel
import dev.scriptor.io.channels.SocketChannel
import dev.scriptor.net.InetSocketAddress
import dev.scriptor.server.*
import dev.scriptor.server.result.Result
import dev.scriptor.server.result.UnitResult
import dev.scriptor.sys.Thread
import dev.scriptor.util.Log
import dev.scriptor.util.Timer
import dev.scriptor.util.TimerTask
import kotlin.time.Duration

class Server(
    val log: Log,
    val provider: Provider = Provider(),
    val hostname: String = "0.0.0.0",
    val port: Int = 8080,
) : AutoCloseable {

    private val server = ServerSocketChannel.open()

    private val routes = mutableMapOf<Method, MutableList<Route>>()

    private val timer = Timer(daemon = true)
    private val tasks = mutableMapOf<String, TimerTask>()

    private var running: Boolean = false

    init {
        server.bind(InetSocketAddress(hostname, port))

        log.info("server listening on $hostname:$port")
    }

    override fun close() {
        server.close()

        tasks.values.forEach(TimerTask::cancel)
        timer.cancel()
    }

    fun register(
        method: Method,
        path: Path,
        accept: String,
        result: String,
        callback: (Provider, Request) -> Result,
    ) {
        routes.computeIfAbsent(method) { mutableListOf() } += Route(
            PathExpression(path),
            method,
            accept,
            result,
            callback,
        )
    }

    fun register(
        name: String,
        delay: Duration,
        period: Duration? = null,
        callback: () -> Unit,
    ) {
        val task = TimerTask.from(callback)
        if (period == null)
            timer.schedule(task, delay)
        else
            timer.scheduleFixed(
                task,
                delay,
                period,
            )
        tasks[name] = task
    }

    fun cancel(name: String) {
        val task = tasks.remove(name) ?: return
        task.cancel()
    }

    fun spin() {
        val socket = server.accept()

        Thread { socket.use(this::handle) }
    }

    fun start() {
        running = true

        while (running && !Thread.current.interrupted) {
            spin()
        }
    }

    fun stop() {
        running = false
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
                .filter { (_, value) -> value.any { it.path.matches(request.path) } }
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

    private fun getResult(request: Request): Result {

        val route = routes
            .computeIfAbsent(request.method) { mutableListOf() }
            .filter { it.path.matches(request.path) }
            .maxOrNull()

        if (route == null) {
            return NotFoundSignal().generate()
        }

        try {
            val result = route(provider, request)

            return Result(
                result.statusCode,
                result.statusText,
                if (route.result == "*/*")
                    result.contentType
                else route.result,
                result.headers,
                result.channel,
                result.position,
                result.count,
            )
        } catch (s: Signal) {
            return s.generate()
        } catch (t: Throwable) {
            log.error(t.stackTraceToString())
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
