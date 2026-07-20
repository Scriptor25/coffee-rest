package dev.scriptor.server.http

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

data class HTTPResponseMessage(
    val protocol: String,
    val statusCode: Int,
    val statusText: String,
    val headers: Map<String, String>,
    val chunked: Boolean,
    val count: Long,
    val body: ReadableByteChannel?,
) : AutoCloseable {

    private fun writeString(channel: WritableByteChannel, value: String) {
        val buffer = ByteBuffer.wrap(value.encodeToByteArray())

        while (buffer.hasRemaining()) {
            channel.write(buffer)
        }
    }

    fun write(channel: WritableByteChannel): HTTPResponseMessage {
        writeString(channel, "$protocol $statusCode $statusText\r\n")

        for ((key, value) in headers) {
            writeString(channel, "$key: $value\r\n")
        }

        writeString(channel, "\r\n")

        if (body != null) {
            if (!chunked) {
                if (body is FileChannel) {
                    val limit = body.size() - body.position()

                    var position = body.position()
                    var remaining =
                        if (count < 0) limit
                        else minOf(count, limit)

                    while (remaining > 0) {
                        val written = body.transferTo(position, remaining, channel)
                        if (written < 0L) break
                        if (written == 0L) continue

                        position += written
                        remaining -= written
                    }
                } else {
                    val buffer = ByteBuffer.allocateDirect(1024 * 1024)

                    var i = 0L
                    while (i < count) {
                        buffer.clear()

                        val read = body.read(buffer)
                        if (read < 0) break
                        if (read == 0) continue

                        buffer.flip()

                        while (buffer.hasRemaining()) {
                            channel.write(buffer)
                        }

                        i += read
                    }
                }
            } else {
                val buffer = ByteBuffer.allocateDirect(1024 * 1024)

                while (true) {
                    buffer.clear()

                    val read = body.read(buffer)
                    if (read < 0) break
                    if (read == 0) continue

                    buffer.flip()

                    writeString(channel, "${read.toString(0x10)}\r\n")

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
