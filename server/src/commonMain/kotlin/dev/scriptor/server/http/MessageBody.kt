package dev.scriptor.server.http

import dev.scriptor.io.MutableBuffer
import dev.scriptor.io.channels.FileChannel
import dev.scriptor.io.channels.ReadableByteChannel
import dev.scriptor.io.channels.WritableByteChannel

data class MessageBody(
    val channel: ReadableByteChannel,
    val position: Long,
    val count: Long,
    val chunked: Boolean,
) : AutoCloseable {

    fun write(destination: WritableByteChannel) {
        if (!chunked) {
            return when (channel) {
                is FileChannel -> {
                    val limit = channel.size - position

                    var pos = position
                    var rem =
                        if (count < 0L) limit
                        else minOf(count, limit)

                    while (rem > 0L) {
                        val transferred = channel.transferTo(destination, pos, rem)
                        if (transferred < 0L) break
                        if (transferred == 0L) continue

                        pos += transferred
                        rem -= transferred
                    }
                }

                else -> {
                    val buffer = MutableBuffer.allocate(8192)

                    var rem = count

                    while (rem > 0L || count < 0L) {
                        buffer.clear()

                        if (rem > 0L) {
                            buffer.limit = minOf(buffer.capacity.toLong(), rem).toInt()
                        }

                        val read = channel.read(buffer)
                        if (read < 0) break
                        if (read == 0) continue

                        buffer.flip()

                        while (buffer.remaining != 0) {
                            val written = destination.write(buffer)
                            if (written < 0) error("unexpected end of stream")
                        }

                        if (rem > 0L) {
                            rem -= read
                        }
                    }
                }
            }
        }

        val buffer = MutableBuffer.allocate(8192)
        val writer = destination.writer()

        while (true) {
            buffer.clear()

            val read = channel.read(buffer)
            if (read < 0L) break
            if (read == 0) continue

            buffer.flip()

            writer.write("${read.toHexString()}\r\n")
            writer.flush()

            while (buffer.remaining != 0) {
                val written = destination.write(buffer)
                if (written < 0L) error("unexpected end of stream")
            }

            writer.write("\r\n")
            writer.flush()
        }

        writer.write("0\r\n\r\n")
        writer.flush()
    }

    override fun close() {
        channel.close()
    }
}
