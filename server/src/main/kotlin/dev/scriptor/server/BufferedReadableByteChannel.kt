package dev.scriptor.server

import java.io.EOFException
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

class BufferedReadableByteChannel(
    private val channel: ReadableByteChannel,
    capacity: Int = 8192
) : ReadableByteChannel {
    private val buffer: ByteBuffer = ByteBuffer
        .allocateDirect(capacity)
        .flip()

    override fun isOpen(): Boolean = channel.isOpen

    override fun close() = channel.close()

    fun readLine(): String? {
        val line = StringBuilder()

        var any = false

        while (true) {
            val b = read()
            if (b < 0) {
                return if (any)
                    line.toString()
                else null
            }

            any = true

            when (b) {
                '\r'.code -> {
                    val next = read()
                    if (next != '\n'.code) {
                        throw EOFException("expected lf after cr")
                    }
                    return line.toString()
                }

                '\n'.code -> {
                    return line.toString()
                }

                else -> {
                    line.append(b.toChar())
                }
            }
        }
    }

    override fun read(dst: ByteBuffer): Int {
        var total = 0

        while (dst.hasRemaining()) {
            if (!buffer.hasRemaining()) {
                if (total > 0) {
                    return total
                }

                return channel.read(dst)
            }

            val n = minOf(buffer.remaining(), dst.remaining())

            val pre = buffer.limit()
            buffer.limit(buffer.position() + n)

            dst.put(buffer)

            buffer.limit(pre)

            total += n
        }

        return total
    }

    private fun fill(): Boolean {
        if (buffer.hasRemaining()) {
            return true
        }

        buffer.clear()

        val n = channel.read(buffer)
        if (n < 0) {
            return false
        }

        buffer.flip()
        return true
    }

    private fun read(): Int {
        if (!fill()) {
            return -1
        }

        return buffer.get().toInt() and 0xff
    }
}