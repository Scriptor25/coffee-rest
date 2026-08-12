package dev.scriptor.io.channels

import dev.scriptor.io.Buffer
import dev.scriptor.io.MutableBuffer
import dev.scriptor.io.transfer

class ReadableBufferChannel(
    private val buffer: Buffer,
) : ReadableByteChannel {
    override var open: Boolean = true

    constructor(array: ByteArray) : this(Buffer.wrap(array))

    override fun read(destination: MutableBuffer): Int {
        if (!open) error("channel is not open")
        if (buffer.remaining == 0) return -1
        return transfer(destination, buffer)
    }

    override fun close() {
        open = false
    }
}
