package dev.scriptor.server.http

import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

class HTTPRequestMessageReader(private val channel: ReadableByteChannel) {

    private val buffer = ByteBuffer
        .allocateDirect(8192)
        .limit(0)

    private fun readByte(): Int {
        if (!buffer.hasRemaining()) {
            buffer.clear()

            val count = channel.read(buffer)

            buffer.flip()

            if (count < 0) {
                return -1
            }
        }

        return buffer.get().toInt() and 0xff
    }

    private fun readLine(): String? {
        val line = StringBuilder()

        var c = readByte()
        if (c < 0) {
            return null
        }

        if (c == '\n'.code) {
            return ""
        }

        do line.append(c.toChar())
        while ((readByte().also { c = it }) > 0 && c != '\n'.code)

        return line.toString()
    }

    fun read(): HTTPRequestMessage? {
        var line = readLine() ?: return null

        line = line.trim { it <= ' ' }
        if (line.isEmpty()) return null

        val request: Array<String> = line.split("\\s+".toRegex()).toTypedArray()
        val method = HTTPMethod.valueOf(request[0])
        val uri = URI.create(request[1])
        val path = uri.path
        val protocol: String = request[2]

        val query: MutableMap<String, MutableList<String>> = HashMap()

        if (uri.query != null) {
            val params = uri.query.split("&+".toRegex()).toTypedArray()
            for (param in params) {
                if ("=" in param) {
                    val pair = param.split("=".toRegex(), limit = 2).toTypedArray()
                    query.computeIfAbsent(pair[0].lowercase()) { mutableListOf() }.add(pair[1])
                }
            }
        }

        val headers: MutableMap<String, MutableList<String>> = HashMap()

        while (true) {
            line = readLine() ?: break

            line = line.trim { it <= ' ' }
            if (line.isEmpty()) break

            val header = line.split(":\\s*".toRegex(), limit = 2).toTypedArray()
            headers.computeIfAbsent(header[0].lowercase()) { mutableListOf() }.add(header[1])
        }

        return HTTPRequestMessage(method, path, ParameterList(query), protocol, ParameterList(headers), channel)
    }
}