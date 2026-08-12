package dev.scriptor.io.channels

import dev.scriptor.io.Buffer

interface GatheringByteChannel : WritableByteChannel {
    fun write(
        sources: Array<Buffer>,
        offset: Int = 0,
        length: Int = sources.size,
    ): Long {
        var total = 0L
        for (index in 0 until length) {
            val source = sources[index + offset]
            val count = write(source)
            if (count < 0) return -1L
            total += count
        }
        return total
    }
}
