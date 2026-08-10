package dev.scriptor.io.channels

import dev.scriptor.io.MutableBuffer

class BufferedReadableByteChannel(
    private val channel: ReadableByteChannel,
    private val buffer: MutableBuffer,
) : ReadableByteChannel {

    override val open: Boolean
        get() = channel.open

    constructor(channel: ReadableByteChannel, capacity: Long = 8192L) : this(channel, MutableBuffer.allocate(capacity))

    constructor(channel: ReadableByteChannel, array: ByteArray) : this(channel, MutableBuffer.wrap(array))

    fun readLine(): String {
        TODO()
    }

    override fun read(destination: MutableBuffer): Long {
        TODO()
    }

    override fun close() {
        channel.close()
    }
}
