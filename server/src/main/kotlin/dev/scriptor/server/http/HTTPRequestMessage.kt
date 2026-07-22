package dev.scriptor.server.http

import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

data class HTTPRequestMessage(
    val method: HTTPMethod,
    val path: String,
    val query: Map<String, MutableList<String>>,
    val protocol: String,
    val headers: Map<String, String>,
    val body: ReadableByteChannel
) {
    companion object {

        private fun pop(channel: ReadableByteChannel): Int {
            val buffer = ByteBuffer.allocateDirect(1)
            if (channel.read(buffer) < 0) {
                return -1
            }
            return buffer[0].toInt()
        }

        private fun readLine(channel: ReadableByteChannel): String? {
            val line = StringBuilder()

            var c = pop(channel)
            if (c < 0) {
                return null
            }

            if (c == '\n'.code) {
                return ""
            }

            do line.append(c.toChar())
            while ((pop(channel).also { c = it }) > 0 && c != '\n'.code)

            return line.toString()
        }

        fun read(channel: ReadableByteChannel): HTTPRequestMessage? {
            var line = readLine(channel)

            if (line == null) {
                return null
            }

            line = line.trim { it <= ' ' }

            val request: Array<String> = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val method = HTTPMethod.valueOf(request[0])
            val uri = URI.create(request[1])
            val path = uri.path
            val protocol: String = request[2]

            val query: MutableMap<String, MutableList<String>> = HashMap()

            if (uri.query != null) {
                val params = uri.query.split("&+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (param in params) {
                    if ("=" in param) {
                        val pair = param.split("=".toRegex(), limit = 2).toTypedArray()
                        query.computeIfAbsent(pair[0].lowercase()) { ArrayList() }.add(pair[1])
                    }
                }
            }

            val headers: MutableMap<String, String> = HashMap()

            while (true) {
                line = readLine(channel)
                if (line == null) {
                    break
                }

                line = line.trim { it <= ' ' }
                if (line.isEmpty()) {
                    break
                }

                val header = line.split(":\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                headers[header[0].lowercase()] = header[1]
            }

            return HTTPRequestMessage(method, path, query, protocol, headers, channel)
        }
    }
}
