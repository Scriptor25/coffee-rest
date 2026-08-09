package dev.scriptor.server.http

import dev.scriptor.stdlib.io.WriteableChannel
import dev.scriptor.stdlib.io.write

class ResponseWriter(
    private val channel: WriteableChannel,
) {
    private fun sanitize(str: String, esc: Map<Char, String>): String {
        var res = String()
        for (c in str) {
            res += if (c in esc) esc[c] else c
        }
        return res
    }

    fun write(response: Response) {
        channel.write("${response.protocol} ${response.statusCode} ${response.statusText}\r\n")

        for ((key, values) in response.headers) {
            val sanKey = sanitize(key, mapOf((':' to "\\:"), ('\r' to "\\r"), ('\n' to "\\n")))
            for (value in values) {
                val sanVal = sanitize(value, mapOf(('\r' to "\\r"), ('\n' to "\\n")))
                channel.write("$sanKey: $sanVal\r\n")
            }
        }

        channel.write("\r\n")

        response.body?.write(channel)
    }
}
