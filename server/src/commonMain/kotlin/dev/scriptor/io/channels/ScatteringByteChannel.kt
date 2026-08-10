package dev.scriptor.io.channels

import dev.scriptor.io.MutableBuffer

interface ScatteringByteChannel : ReadableByteChannel {
    fun read(
        destinations: Array<MutableBuffer>,
        offset: Int = 0,
        length: Int = destinations.size,
    ): Long
}
