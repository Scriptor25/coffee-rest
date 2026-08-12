package dev.scriptor.io.channels

interface SeekableBytechannel : ByteChannel {
    val size: Long
    var position: Long

    fun truncate(size: Long): SeekableBytechannel
}
