package dev.scriptor.io.channels

import dev.scriptor.net.SocketAddress
import dev.scriptor.net.SocketOption

interface NetworkChannel : Channel {

    interface Options  {
        val supported: Set<SocketOption<*>>

        operator fun <T : Any> get(name: SocketOption<T>): T
        operator fun <T : Any> set(name: SocketOption<T>, value: T)
    }

    val local: SocketAddress?
    val options: Options

    fun bind(address: SocketAddress): NetworkChannel
}
