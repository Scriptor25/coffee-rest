package dev.scriptor.stdlib.io

interface Channel : AutoCloseable {

    /**
     * total size of this channel, or `-1` if unknown.
     */
    val size: Long
}

interface ReadableChannel : Channel {

    /**
     * read at least zero, at most `destination.remaining` bytes.
     * returns the number of read bytes, or `-1` if end of file has been reached.
     */
    fun read(destination: MutableBuffer): Long

    /**
     * transfer at least zero, at most `count` bytes.
     * returns the number of transferred bytes, or `-1` if end of file has been reached.
     */
    fun transferTo(destination: WriteableChannel, count: Long): Long
}

interface WriteableChannel : Channel {

    /**
     * write at least zero, at most `source.remaining` bytes.
     * returns the number of written bytes, or `-1` if end of file has been reached.
     */
    fun write(source: Buffer): Long
}

fun WriteableChannel.write(string: String) {
    val buffer = Buffer(string.encodeToByteArray())
    while (buffer.remaining != 0) {
        write(buffer)
    }
}
