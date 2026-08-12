package dev.scriptor.io.channels

import dev.scriptor.net.SocketAddress

interface SocketChannel : ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel {
    val remote: SocketAddress?
}
