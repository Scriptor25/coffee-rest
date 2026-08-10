package dev.scriptor.io.channels

interface ServerSocketChannel : SelectableChannel, NetworkChannel {

    companion object {
        fun open(): ServerSocketChannel {
            return openServerSocketChannel()
        }
    }

    fun accept(): SocketChannel
}

internal expect fun openServerSocketChannel(): ServerSocketChannel
