package dev.scriptor.server

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

interface RangeReadableByteChannel : ReadableByteChannel {

    val channel: ReadableByteChannel
    val range: LongRange

    val position: Long
    val remaining: Long

    override fun isOpen(): Boolean {
        return channel.isOpen
    }

    override fun close() {
        channel.close()
    }

    override fun read(dst: ByteBuffer): Int {
        if (remaining == 0L) {
            return -1
        }

        val count = minOf(dst.remaining().toLong(), remaining).toInt()
        val limit = dst.limit()
        dst.limit(dst.position() + count)
        val read = channel.read(dst)
        dst.limit(limit)
        return read
    }

    fun transferTo(target: WritableByteChannel): Long
}

fun RangeReadableByteChannel(channel: ReadableByteChannel, range: LongRange): RangeReadableByteChannel {
    return when (channel) {
        is FileChannel -> {
            val start = maxOf(range.start, channel.position())
            val endExclusive = minOf(range.endExclusive, channel.size())
            val range = LongRange(start, endExclusive);

            object : RangeReadableByteChannel {

                private var index = range.start

                override val channel = channel
                override val range = range

                override val position: Long
                    get() = index
                override val remaining: Long
                    get() = range.endExclusive - index

                override fun transferTo(target: WritableByteChannel): Long {
                    if (remaining == 0L) {
                        return -1L
                    }

                    val transferred = channel.transferTo(position, remaining, target)
                    channel.position(position + transferred)
                    return transferred
                }
            }
        }

        else -> object : RangeReadableByteChannel {

            private var index = range.start

            override val channel = channel
            override val range = range

            override val position: Long
                get() = index
            override val remaining: Long
                get() = range.endExclusive - index

            override fun transferTo(target: WritableByteChannel): Long {
                if (remaining == 0L) {
                    return -1L
                }

                val count = minOf(remaining, Integer.MAX_VALUE.toLong()).toInt()

                val buffer = ByteBuffer.allocate(count)
                if (read(buffer) < 0) {
                    return -1L
                }

                buffer.flip()

                while (buffer.hasRemaining()) {
                    if (target.write(buffer) < 0) {
                        return -1L
                    }
                }

                return buffer.limit().toLong()
            }
        }
    }
}
