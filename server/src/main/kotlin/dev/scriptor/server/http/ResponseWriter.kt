package dev.scriptor.server.http

import java.nio.channels.Channels
import java.nio.channels.WritableByteChannel

class ResponseWriter(
    private val channel: WritableByteChannel,
) {
    private fun sanitize(str: String, esc: Map<Char, CharSequence>): String {
        var res = String()
        for (c in str) {
            res += if (c in esc) esc[c] else c
        }
        return res
    }

    fun write(response: Response) {
        if (response.protocol == Version.HTTP_0_9) {
            response.body?.write(channel)
            return
        }

        val writer = Channels.newWriter(channel, Charsets.UTF_8)

        writer.write("${response.protocol} ${response.statusCode} ${response.statusText}\r\n")

        for ((key, values) in response.headers) {
            val sanKey = sanitize(key, mapOf(Pair(':', "\\:"), Pair('\r', "\\r"), Pair('\n', "\\n")))
            for (value in values) {
                val sanVal = sanitize(value, mapOf(Pair('\r', "\\r"), Pair('\n', "\\n")))
                writer.write("$sanKey: $sanVal\r\n")
            }
        }

        writer.write("\r\n")
        writer.flush()

        response.body?.write(channel)
    }
}
