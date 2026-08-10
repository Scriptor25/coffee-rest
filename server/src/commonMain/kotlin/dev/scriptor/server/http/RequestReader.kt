package dev.scriptor.server.http

import dev.scriptor.computeIfAbsent
import dev.scriptor.io.Path
import dev.scriptor.io.channels.BufferedReadableByteChannel
import dev.scriptor.net.URI
import dev.scriptor.server.ParameterList

class RequestReader(
    private val channel: BufferedReadableByteChannel,
) {
    fun read(): Request? {
        var line = channel.readLine() ?: return null

        line = line.trim { it <= ' ' }
        if (line.isEmpty()) return null

        val request: Array<String> = line.split("\\s+".toRegex()).toTypedArray()
        val method = Method.valueOf(request[0])
        val uri = URI.parse(request[1])
        val path = Path(uri.pathname)
        val protocol: String = request[2]

        val queryMap = mutableMapOf<String, MutableList<String>>()

        val query = uri.query.split("&+".toRegex()).toTypedArray()
        for (param in query) {
            if ("=" in param) {
                val (key, value) = param.split("=".toRegex(), 2).toTypedArray()
                queryMap.computeIfAbsent(key.lowercase()) { mutableListOf() }.add(value)
            }
        }

        val headerMap = mutableMapOf<String, MutableList<String>>()

        while (true) {
            line = channel.readLine() ?: break

            line = line.trim { it <= ' ' }
            if (line.isEmpty()) break

            val header = line.split(":\\s*".toRegex(), 2).toTypedArray()
            headerMap.computeIfAbsent(header[0].lowercase()) { mutableListOf() }.add(header[1])
        }

        val headers = ParameterList(headerMap)

        val count: Long
        val chunked: Boolean
        if ("content-length" in headers) {
            count = headers["content-length"]!!.toLong()
            chunked = false
        } else if ("transfer-encoding" in headers && headers["transfer-encoding"]?.lowercase() == "chunked") {
            count = -1L
            chunked = true
        } else {
            count = -1L
            chunked = false
        }

        return Request(
            method,
            path,
            ParameterList(queryMap),
            protocol,
            headers,
            MessageBody(channel, 0L, count, chunked),
        )
    }
}