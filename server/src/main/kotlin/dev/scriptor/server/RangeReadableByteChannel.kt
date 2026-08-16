package dev.scriptor.server

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel

interface RangeReadableByteChannel : ReadableByteChannel {

    val channel: ReadableByteChannel
    val range: LongRange

    val position: Long

    val size: Long
        get() = range.last + 1 - range.first
    val remaining: Long
        get() = range.last + 1 - position

    override fun isOpen(): Boolean {
        return channel.isOpen
    }

    override fun close() {
        channel.close()
    }

    override fun read(dst: ByteBuffer): Int

    fun transferTo(target: WritableByteChannel): Long
}

fun RangeReadableByteChannel(channel: ReadableByteChannel, range: LongRange): RangeReadableByteChannel {
    return when (channel) {
        is FileChannel -> {
            val begin = maxOf(range.first, channel.position())
            val end = minOf(range.last + 1L, channel.size())
            val range = begin until end

            channel.position(begin)

            object : RangeReadableByteChannel {

                override val channel = channel
                override val range = range

                override val position: Long
                    get() = channel.position()

                override fun read(dst: ByteBuffer): Int {
                    if (position !in range) {
                        return -1
                    }

                    val count = minOf(dst.remaining().toLong(), remaining).toInt()

                    val limit = dst.limit()
                    dst.limit(dst.position() + count)

                    val read = channel.read(dst)

                    dst.limit(limit)

                    return read
                }

                override fun transferTo(target: WritableByteChannel): Long {
                    if (position !in range) {
                        return -1L
                    }

                    val transferred = channel.transferTo(position, remaining, target)
                    channel.position(position + transferred)
                    return transferred
                }
            }
        }

        else -> object : RangeReadableByteChannel {

            override val channel = channel
            override val range = range

            override var position = range.first

            override fun read(dst: ByteBuffer): Int {
                if (position !in range) {
                    return -1
                }

                val count = minOf(dst.remaining().toLong(), remaining).toInt()

                val limit = dst.limit()
                dst.limit(dst.position() + count)

                val read = channel.read(dst)

                dst.limit(limit)

                if (read >= 0) {
                    position += read
                }

                return read
            }

            override fun transferTo(target: WritableByteChannel): Long {
                if (position !in range) {
                    return -1L
                }

                val count = minOf(remaining, Integer.MAX_VALUE.toLong()).toInt()

                val buffer = ByteBuffer.allocate(count)

                val read = channel.read(buffer)

                if (read >= 0) {
                    position += read
                } else {
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
