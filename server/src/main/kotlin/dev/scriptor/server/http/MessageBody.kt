package dev.scriptor.server.http

import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

data class MessageBody(
    val channel: ReadableByteChannel,
    val position: Long,
    val count: Long,
    val chunked: Boolean,
) : AutoCloseable {

    override fun close() {
        channel.close()
    }

    fun write(channel: WritableByteChannel) {
        val writer = Channels.newWriter(channel, Charsets.UTF_8)

        if (!chunked) {
            if (this.channel is FileChannel) {
                val limit = this.channel.size() - position

                var pos = position
                var rem =
                    if (count < 0L) limit
                    else minOf(count, limit)

                while (rem > 0L) {
                    val n = this.channel.transferTo(pos, rem, channel)
                    if (n < 0L) break
                    if (n == 0L) continue

                    pos += n
                    rem -= n
                }
            } else {
                skip(position)

                val buffer = ByteBuffer.allocateDirect(8192)
                var rem = count

                while (rem > 0L || count < 0L) {
                    buffer.clear()

                    if (rem > 0L) {
                        buffer.limit(minOf(buffer.capacity().toLong(), rem).toInt())
                    }

                    val n = this.channel.read(buffer)
                    if (n < 0) break
                    if (n == 0) continue

                    buffer.flip()

                    while (buffer.hasRemaining()) {
                        channel.write(buffer)
                    }

                    if (rem > 0L) {
                        rem -= n.toLong()
                    }
                }
            }
        } else {
            val buffer = ByteBuffer.allocateDirect(8192)

            while (true) {
                buffer.clear()

                val n = this.channel.read(buffer)
                if (n < 0) break
                if (n == 0) continue

                buffer.flip()

                writer.write("${n.toString(0x10)}\r\n")
                writer.flush()

                while (buffer.hasRemaining()) {
                    channel.write(buffer)
                }

                writer.write("\r\n")
                writer.flush()
            }

            writer.write("0\r\n\r\n")
            writer.flush()
        }
    }

    private fun skip(count: Long) {
        var rem = count
        val buffer = ByteBuffer.allocateDirect(8192)

        while (rem > 0L) {
            buffer.clear()
            buffer.limit(minOf(buffer.capacity().toLong(), rem).toInt())

            val n = channel.read(buffer)
            if (n < 0) error("unexpected end of file")

            rem -= n.toLong()
        }
    }
}
