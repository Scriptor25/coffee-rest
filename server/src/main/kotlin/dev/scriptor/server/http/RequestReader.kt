package dev.scriptor.server.http

import dev.scriptor.server.BufferedReadableByteChannel
import dev.scriptor.server.ParameterList
import dev.scriptor.server.RangeReadableByteChannel
import java.net.URI
import java.nio.channels.ReadableByteChannel

class RequestReader(
    private val channel: BufferedReadableByteChannel,
) {
    fun read(): Request? {
        var line = channel.readLine()?.trim()
        if (line.isNullOrEmpty()) return null

        val request = line.split("\\s+".toRegex(), limit = 3)
        if (request.size < 2) error("invalid HTTP request line '$line'")

        val method = Method.valueOf(request[0])

        val targetURI = if (request[1] == "*") null else URI(request[1])

        val protocol = when {
            request.size == 2 -> Version.HTTP_0_9
            request[2] == "HTTP/1.0" -> Version.HTTP_1_0
            request[2] == "HTTP/1.1" -> Version.HTTP_1_1
            else -> error("unsupported HTTP protocol '${request[2]}'")
        }

        var queryString: String? = null
        var origin: String? = null

        val target = when (method) {
            Method.CONNECT if targetURI != null -> {
                origin = targetURI.authority

                AuthorityRequestTarget(targetURI.host, targetURI.port)
            }

            Method.OPTIONS if targetURI == null -> AsteriskRequestTarget

            else if targetURI != null -> {
                if (targetURI.isAbsolute) {
                    origin = targetURI.authority
                }

                queryString = targetURI.query
                OriginRequestTarget(targetURI.path)
            }

            else -> error("unsupported HTTP method '$method' or target '${request[1]}'")
        }

        val queryList = buildList {
            if (queryString != null) {
                val params = queryString.split("&+".toRegex())
                for (param in params) {
                    if ("=" in param) {
                        val (key, value) = param.split("=", limit = 2)

                        this += key to value
                    }
                }
            }
        }

        val query = ParameterList(queryList)

        val headers: ParameterList

        if (protocol == Version.HTTP_0_9) {
            headers = ParameterList()
        } else {
            val headerList = buildList {
                while (true) {
                    line = channel.readLine()?.trim()
                    if (line.isNullOrEmpty()) break

                    val (key, value) = line.split(":\\s*".toRegex(), limit = 2)

                    this += key to value
                }
            }

            headers = ParameterList(headerList)
        }

        val source: ReadableByteChannel
        val chunked: Boolean

        if ("content-length" in headers) {
            val length = headers["content-length"]!!.toLong()
            source = RangeReadableByteChannel(channel, 0L until length)
            chunked = false
        } else if (headers["transfer-encoding"]?.lowercase() == "chunked") {
            source = channel
            chunked = true
        } else {
            source = channel
            chunked = false
        }

        if (origin != null && "host" in headers) {
            val host = headers["host"]
            if (host != origin) {
                error("host '$host' != origin '$origin'")
            }
        }

        val body = MessageBody(source, chunked)

        return Request(
            method,
            target,
            protocol,
            query,
            headers,
            body,
        )
    }
}