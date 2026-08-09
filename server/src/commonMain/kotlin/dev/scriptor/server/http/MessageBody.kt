package dev.scriptor.server.http

import dev.scriptor.stdlib.io.MutableBuffer
import dev.scriptor.stdlib.io.ReadableChannel
import dev.scriptor.stdlib.io.WriteableChannel
import dev.scriptor.stdlib.io.write

data class MessageBody(
    val channel: ReadableChannel,
    val position: Long,
    val count: Long,
    val chunked: Boolean,
) : AutoCloseable {

    fun write(destination: WriteableChannel) {
        if (!chunked) {
            val limit = channel.size - position

            var pos = position
            var rem =
                if (count < 0L) limit
                else minOf(count, limit)

            while (rem > 0L) {
                val n = channel.transferTo(destination, rem)
                if (n < 0L) break
                if (n == 0L) continue

                pos += n
                rem -= n
            }

            return
        }

        val buffer = MutableBuffer(8192)

        while (true) {
            buffer.clear()

            val n = channel.read(buffer)
            if (n < 0L) break
            if (n == 0L) continue

            buffer.flip()

            destination.write("${n.toHexString()}\r\n")

            while (buffer.remaining != 0) {
                destination.write(buffer)
            }

            destination.write("\r\n")
        }

        destination.write("0\r\n\r\n")
    }

    override fun close() {
        channel.close()
    }
}
