package dev.scriptor.io.channels

import dev.scriptor.io.MutableBuffer

class BufferedReadableByteChannel(
    private val channel: ReadableByteChannel,
    private val buffer: MutableBuffer,
) : ReadableByteChannel {

    override val open: Boolean
        get() = channel.open

    constructor(channel: ReadableByteChannel, capacity: Int = 8192) : this(channel, MutableBuffer.allocate(capacity))

    constructor(channel: ReadableByteChannel, array: ByteArray) : this(channel, MutableBuffer.wrap(array))

    fun readLine(): String? {
        TODO()
    }

    override fun read(destination: MutableBuffer): Int {
        TODO()
    }

    override fun close() {
        channel.close()
    }
}
