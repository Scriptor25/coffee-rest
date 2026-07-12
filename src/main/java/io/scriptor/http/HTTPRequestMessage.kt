package io.scriptor.http

import java.io.InputStream
import java.net.URI
import java.util.*

data class HTTPRequestMessage(
    val method: HTTPMethod,
    val path: String,
    val query: MutableMap<String, MutableList<String>>,
    val protocol: String,
    val headers: MutableMap<String, String>,
    val body: InputStream
) {
    companion object {

        private fun readLine(stream: InputStream): String? {
            val line = StringBuilder()

            var c = stream.read()
            if (c < 0) {
                return null
            }

            if (c == '\n'.code) {
                return ""
            }

            do line.append(c.toChar())
            while ((stream.read().also { c = it }) > 0 && c != '\n'.code)

            return line.toString()
        }

        fun read(stream: InputStream): HTTPRequestMessage? {
            var line = readLine(stream)

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
                    if (param.contains("=")) {
                        val pair = param.split("=".toRegex(), limit = 2).toTypedArray()
                        query.computeIfAbsent(pair[0]) { ArrayList() }.add(pair[1])
                    }
                }
            }

            val headers: MutableMap<String, String> = HashMap()

            while (true) {
                line = readLine(stream)
                if (line == null) {
                    break
                }

                line = line.trim { it <= ' ' }
                if (line.isEmpty()) {
                    break
                }

                val header = line.split(":\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                headers[header[0].lowercase(Locale.getDefault())] = header[1]
            }

            return HTTPRequestMessage(method, path, query, protocol, headers, stream)
        }
    }
}
