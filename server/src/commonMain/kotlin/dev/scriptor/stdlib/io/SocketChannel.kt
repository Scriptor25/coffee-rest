package dev.scriptor.stdlib.io

interface SocketAddress

class INetSocketAddress(hostname: String, port: Int) : SocketAddress

interface SocketChannel : ReadableChannel, WriteableChannel

class ServerSocketChannel : Channel {

    override val size: Long
        get() = TODO()

    fun bind(address: SocketAddress) {
        TODO()
    }

    fun accept(): SocketChannel {
        TODO()
    }

    override fun close() {
        TODO()
    }
}
