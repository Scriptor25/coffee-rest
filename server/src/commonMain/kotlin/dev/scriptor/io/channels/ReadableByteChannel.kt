package dev.scriptor.io.channels

import dev.scriptor.io.ChannelReader
import dev.scriptor.io.MutableBuffer

interface ReadableByteChannel : Channel {
    fun read(destination: MutableBuffer): Long

    fun reader(): ChannelReader {
        return ChannelReader(this)
    }
}
