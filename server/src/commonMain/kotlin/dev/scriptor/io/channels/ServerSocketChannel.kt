package dev.scriptor.io.channels

interface ServerSocketChannel : SelectableChannel, NetworkChannel {

    companion object {
        fun open(): ServerSocketChannel {
            TODO()
        }
    }

    fun accept(): SocketChannel
}
