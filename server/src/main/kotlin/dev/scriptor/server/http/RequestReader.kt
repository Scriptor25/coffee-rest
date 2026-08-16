package dev.scriptor.server.http

import dev.scriptor.server.BufferedReadableByteChannel
import dev.scriptor.server.ParameterList
import dev.scriptor.server.RangeReadableByteChannel
import java.net.URI
import java.nio.channels.ReadableByteChannel
import kotlin.io.path.Path

class RequestReader(
    private val channel: BufferedReadableByteChannel,
) {
    fun read(): Request? {
        var line = channel.readLine()?.trim()
        if (line.isNullOrEmpty()) return null

        val request = line.split("\\s+".toRegex())

        val method = Method.valueOf(request[0])

        val uri = URI.create(request[1])
        val path = Path(uri.path).normalize().toString()

        val protocol = when {
            request.size == 2 -> Version.HTTP_0_9
            request[2] == "HTTP/1.0" -> Version.HTTP_1_0
            request[2] == "HTTP/1.1" -> Version.HTTP_1_1
            else -> error("unsupported protocol ${request[2]}")
        }

        val queryList = buildList {
            if (uri.query != null) {
                val params = uri.query.split("&+".toRegex())
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

        val body = MessageBody(source, chunked)

        return Request(
            method,
            path,
            query,
            protocol,
            headers,
            body,
        )
    }
}