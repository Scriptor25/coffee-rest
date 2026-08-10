package dev.scriptor.io.channels

import dev.scriptor.io.MutableBuffer

interface ScatteringByteChannel : ReadableByteChannel {
    fun read(
        destinations: Array<MutableBuffer>,
        offset: Int = 0,
        length: Int = destinations.size,
    ): Long {
        var total = 0L
        for (index in 0 until length) {
            val destination = destinations[index + offset]
            val count = read(destination)
            if (count < 0) return -1L
            total += count
        }
        return total
    }
}
