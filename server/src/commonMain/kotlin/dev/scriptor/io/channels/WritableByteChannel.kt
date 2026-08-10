package dev.scriptor.io.channels

import dev.scriptor.io.Buffer
import dev.scriptor.io.ChannelWriter

interface WritableByteChannel : Channel {
    fun write(source: Buffer): Long

    fun writer(): ChannelWriter {
        return ChannelWriter(this)
    }
}
