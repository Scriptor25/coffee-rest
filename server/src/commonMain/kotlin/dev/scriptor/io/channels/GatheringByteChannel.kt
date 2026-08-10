package dev.scriptor.io.channels

import dev.scriptor.io.Buffer

interface GatheringByteChannel : WritableByteChannel {
    fun write(
        sources: Array<Buffer>,
        offset: Int = 0,
        length: Int = sources.size,
    ): Long
}
