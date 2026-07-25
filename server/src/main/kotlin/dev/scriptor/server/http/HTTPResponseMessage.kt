package dev.scriptor.server.http

import dev.scriptor.server.ParameterList
import java.nio.channels.Channels
import java.nio.channels.WritableByteChannel

data class HTTPResponseMessage(
    val protocol: String,
    val statusCode: Int,
    val statusText: String,
    val headers: ParameterList,
    val body: HTTPMessageBody?,
) : AutoCloseable {

    private fun sanitize(str: String, esc: Map<Char, CharSequence>): String {
        var res = String()
        for (c in str) {
            res +=
                if (c in esc) esc[c]
                else c
        }
        return res
    }

    fun write(channel: WritableByteChannel): HTTPResponseMessage {
        val writer = Channels.newWriter(channel, Charsets.UTF_8)

        writer.write("$protocol $statusCode $statusText\r\n")

        for ((key, values) in headers) {
            val keySan = sanitize(key, mapOf(Pair(':', "\\:"), Pair('\r', "\\r"), Pair('\n', "\\n")))
            for (value in values) {
                val valueSan = sanitize(value, mapOf(Pair('\r', "\\r"), Pair('\n', "\\n")))
                writer.write("$keySan: $valueSan\r\n")
            }
        }

        writer.write("\r\n")
        writer.flush()

        body?.write(channel)

        return this
    }

    override fun close() {
        body?.close()
    }
}
