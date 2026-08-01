package dev.scriptor.server.http

import dev.scriptor.server.BufferedReadableByteChannel
import dev.scriptor.server.ParameterList
import java.net.URI

class RequestReader(
    private val channel: BufferedReadableByteChannel,
) {
    fun read(): Request? {
        var line = channel.readLine() ?: return null

        line = line.trim { it <= ' ' }
        if (line.isEmpty()) return null

        val request: Array<String> = line.split("\\s+".toRegex()).toTypedArray()
        val method = Method.valueOf(request[0])
        val uri = URI.create(request[1])
        val path = uri.path
        val protocol: String = request[2]

        val queryMap: MutableMap<String, MutableList<String>> = HashMap()

        if (uri.query != null) {
            val params = uri.query.split("&+".toRegex()).toTypedArray()
            for (param in params) {
                if ("=" in param) {
                    val pair = param.split("=".toRegex(), limit = 2).toTypedArray()
                    queryMap.computeIfAbsent(pair[0].lowercase()) { mutableListOf() }.add(pair[1])
                }
            }
        }

        val headerMap: MutableMap<String, MutableList<String>> = HashMap()

        while (true) {
            line = channel.readLine() ?: break

            line = line.trim { it <= ' ' }
            if (line.isEmpty()) break

            val header = line.split(":\\s*".toRegex(), limit = 2).toTypedArray()
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