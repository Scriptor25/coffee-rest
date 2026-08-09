package dev.scriptor.stdlib.io

class BufferedReadableChannel(val channel: ReadableChannel) : ReadableChannel {

    val buffer = MutableBuffer(8192).flip()

    override val size: Long
        get() = channel.size

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
                        error("expected lf after cr")
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

    override fun read(destination: MutableBuffer): Long {
        var total = 0L

        while (destination.remaining != 0) {
            if (buffer.remaining == 0) {
                if (total > 0) {
                    return total
                }

                return channel.read(destination)
            }

            val n = minOf(buffer.remaining, destination.remaining)

            val pre = buffer.limit
            buffer.limit = buffer.position + n
            destination.push(buffer)
            buffer.limit = pre

            total += n
        }

        return total
    }

    override fun transferTo(destination: WriteableChannel, count: Long): Long {
        val buffer = MutableBuffer(count.toInt())
        if (read(buffer) < 0L) return -1L
        buffer.flip()
        while (buffer.remaining != 0) {
            val n = destination.write(buffer)
            if (n < 0L) break
        }
        TODO()
    }

    override fun close() {
        channel.close()
    }

    private fun fill(): Boolean {
        if (buffer.remaining != 0) {
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

        if (buffer.remaining == 0) {
            return -1
        }

        return buffer.pop().toInt() and 0xff
    }
}
