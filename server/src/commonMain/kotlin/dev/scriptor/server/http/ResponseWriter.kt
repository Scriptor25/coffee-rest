package dev.scriptor.server.http

import dev.scriptor.io.channels.WritableByteChannel

class ResponseWriter(
    private val channel: WritableByteChannel,
) {
    private fun sanitize(str: String, esc: Map<Char, String>): String {
        var res = StringBuilder()
        for (c in str) {
            res.append(if (c in esc) esc[c] else c)
        }
        return res.toString()
    }

    fun write(response: Response) {
        val writer = channel.writer()

        writer.write("${response.protocol} ${response.statusCode} ${response.statusText}\r\n")

        for ((key, values) in response.headers) {
            val sanKey = sanitize(key, mapOf((':' to "\\:"), ('\r' to "\\r"), ('\n' to "\\n")))
            for (value in values) {
                val sanVal = sanitize(value, mapOf(('\r' to "\\r"), ('\n' to "\\n")))
                writer.write("$sanKey: $sanVal\r\n")
            }
        }

        writer.write("\r\n")
        writer.flush()

        response.body?.write(channel)
    }
}
