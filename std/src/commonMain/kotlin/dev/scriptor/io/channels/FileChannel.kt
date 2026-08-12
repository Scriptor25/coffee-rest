package dev.scriptor.io.channels

import dev.scriptor.io.Buffer
import dev.scriptor.io.MutableBuffer

interface FileChannel : SeekableBytechannel {
    fun read(destination: MutableBuffer, position: Long): Long
    fun write(source: Buffer, position: Long): Long

    fun transferTo(destination: WritableByteChannel, position: Long, count: Long): Long
    fun transferFrom(source: ReadableByteChannel, position: Long, count: Long): Long
}
