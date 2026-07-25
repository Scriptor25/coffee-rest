package dev.scriptor.server.http

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

data class HTTPResponseMessage(
    val protocol: String,
    val statusCode: Int,
    val statusText: String,
    val headers: ParameterList,
    val chunked: Boolean,
    val position: Long,
    val count: Long,
    val body: ReadableByteChannel?,
) : AutoCloseable {

    private fun writeString(channel: WritableByteChannel, value: String) {
        val buffer = ByteBuffer.wrap(value.encodeToByteArray())

        while (buffer.hasRemaining()) {
            channel.write(buffer)
        }
    }

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
        writeString(channel, "$protocol $statusCode $statusText\r\n")

        for ((key, values) in headers) {
            val keySan = sanitize(key, mapOf(Pair(':', "\\:"), Pair('\r', "\\r"), Pair('\n', "\\n")))
            for (value in values) {
                val valueSan = sanitize(value, mapOf(Pair('\r', "\\r"), Pair('\n', "\\n")))
                writeString(channel, "$keySan: $valueSan\r\n")
            }
        }

        writeString(channel, "\r\n")

        if (body != null) {
            if (!chunked) {
                if (body is FileChannel) {
                    val limit = body.size() - position

                    var pos = position
                    var rem =
                        if (count < 0L) limit
                        else minOf(count, limit)

                    while (rem > 0) {
                        val n = body.transferTo(pos, rem, channel)
                        if (n < 0L) break
                        if (n == 0L) continue

                        pos += n
                        rem -= n
                    }
                } else {
                    val buffer = ByteBuffer.allocateDirect(1024 * 1024)

                    var i = 0L
                    while (count < 0 || i < count + position) {
                        buffer.clear()

                        val n = body.read(buffer)
                        if (n < 0) break
                        if (n == 0) continue

                        buffer.flip()

                        if (i < position) {
                            buffer.position(minOf((position - i).toInt(), buffer.limit()))
                        }

                        while (buffer.hasRemaining()) {
                            channel.write(buffer)
                        }

                        i += n
                    }
                }
            } else {
                val buffer = ByteBuffer.allocateDirect(1024 * 1024)

                while (true) {
                    buffer.clear()

                    val n = body.read(buffer)
                    if (n < 0) break
                    if (n == 0) continue

                    buffer.flip()

                    writeString(channel, "${n.toString(0x10)}\r\n")

                    while (buffer.hasRemaining()) {
                        channel.write(buffer)
                    }

                    writeString(channel, "\r\n")
                }

                writeString(channel, "0\r\n\r\n")
            }
        }

        return this
    }

    override fun close() {
        body?.close()
    }
}
