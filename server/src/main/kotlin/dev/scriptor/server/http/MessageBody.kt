package dev.scriptor.server.http

import dev.scriptor.server.RangeReadableByteChannel
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

data class MessageBody(
    val channel: ReadableByteChannel,
    val chunked: Boolean,
) : AutoCloseable {

    override fun close() {
        channel.close()
    }

    fun write(target: WritableByteChannel) {
        val writer = Channels.newWriter(target, Charsets.UTF_8)

        if (!chunked) {
            when (channel) {
                is RangeReadableByteChannel -> {
                    while (true) {
                        val transferred = channel.transferTo(target)
                        if (transferred < 0L) break
                    }
                }

                else -> {
                    val buffer = ByteBuffer.allocateDirect(8192)

                    while (true) {
                        buffer.clear()

                        val count = channel.read(buffer)
                        if (count < 0) break
                        if (count == 0) continue

                        buffer.flip()

                        while (buffer.hasRemaining()) {
                            target.write(buffer)
                        }
                    }
                }
            }
        } else {
            val buffer = ByteBuffer.allocateDirect(8192)

            while (true) {
                buffer.clear()

                val count = channel.read(buffer)
                if (count < 0) break
                if (count == 0) continue

                buffer.flip()

                writer.write(count.toString(0x10))
                writer.write("\r\n")
                writer.flush()

                while (buffer.hasRemaining()) {
                    target.write(buffer)
                }

                writer.write("\r\n")
                writer.flush()
            }

            writer.write("0\r\n\r\n")
            writer.flush()
        }
    }
}
