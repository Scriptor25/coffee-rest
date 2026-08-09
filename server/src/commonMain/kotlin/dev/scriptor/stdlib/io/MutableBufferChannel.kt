package dev.scriptor.stdlib.io

class MutableBufferChannel(val buffer: MutableBuffer) : ReadableChannel, WriteableChannel {

    override val size: Long
        get() = buffer.limit.toLong()

    override fun read(destination: MutableBuffer): Long {
        if (buffer.remaining == 0) return -1L

        val remaining = minOf(buffer.remaining, destination.remaining)
        for (i in 0 until remaining) {
            destination[i] = buffer[i]
        }

        buffer.position += remaining
        destination.position += remaining

        return remaining.toLong()
    }

    override fun write(source: Buffer): Long {
        if (buffer.remaining == 0) return -1L

        val remaining = minOf(buffer.remaining, source.remaining)
        for (i in 0 until remaining) {
            buffer[i] = source[i]
        }

        buffer.position += remaining
        source.position += remaining

        return remaining.toLong()
    }

    override fun transferTo(destination: WriteableChannel, count: Long): Long {
        if (buffer.remaining == 0) return -1L

        if (destination is MutableBufferChannel) {
            val remaining = minOf(count, buffer.remaining.toLong(), destination.buffer.remaining.toLong()).toInt()
            for (i in 0 until remaining) {
                destination.buffer[i] = buffer[i]
            }

            buffer.position += remaining
            destination.buffer.position += remaining

            return remaining.toLong()
        }

        if (count >= 0) {
            val buffer = MutableBuffer(count.toInt())

            read(buffer)

            buffer.flip()

            while (buffer.remaining != 0) {
                val n = destination.write(buffer)
                if (n < 0L) break
            }

            return buffer.limit.toLong()
        }

        TODO()
    }

    override fun close() {
    }
}
