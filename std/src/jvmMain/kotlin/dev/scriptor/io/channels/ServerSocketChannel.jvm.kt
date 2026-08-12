package dev.scriptor.io.channels

import dev.scriptor.io.Buffer
import dev.scriptor.io.MutableBuffer
import dev.scriptor.net.SocketAddress
import dev.scriptor.net.SocketOption
import java.nio.ByteBuffer
import kotlin.reflect.KClass

internal fun <T : Any> socketOption(name: SocketOption<T>): java.net.SocketOption<T> {
    return object : java.net.SocketOption<T> {
        override fun name(): String {
            return name.name
        }

        override fun type(): Class<T> {
            return name.type.java
        }
    }
}

internal fun <T : Any> socketOption(name: java.net.SocketOption<T>): SocketOption<T> {
    return object : SocketOption<T> {
        override val name: String
            get() = name.name()
        override val type: KClass<T>
            get() = name.type().kotlin
    }
}

internal fun socketAddress(address: SocketAddress): java.net.SocketAddress {
    TODO("socket address -> java net socket address")
}

internal fun socketAddress(address: java.net.SocketAddress): SocketAddress {
    TODO("java net socket address -> socket address")
}

internal fun options(channel: java.nio.channels.NetworkChannel): NetworkChannel.Options {
    return object : NetworkChannel.Options {
        override val supported: Set<SocketOption<*>>
            get() = channel.supportedOptions().map { socketOption(it) }.toSet()

        override fun <T : Any> get(name: SocketOption<T>): T {
            return channel.getOption(socketOption(name))
        }

        override fun <T : Any> set(name: SocketOption<T>, value: T) {
            channel.setOption(socketOption(name), value)
        }
    }
}

internal actual fun openServerSocketChannel(): ServerSocketChannel {
    val channel = java.nio.channels.ServerSocketChannel.open()

    return object : ServerSocketChannel {

        override val open: Boolean
            get() = channel.isOpen

        override val local: SocketAddress?
            get() =
                if (channel.localAddress == null) null
                else socketAddress(channel.localAddress)

        override val options: NetworkChannel.Options = options(channel)

        override fun bind(address: SocketAddress): ServerSocketChannel {
            channel.bind(socketAddress(address))
            return this
        }

        override fun accept(): SocketChannel {
            val channel = channel.accept()

            return object : SocketChannel {
                override val open: Boolean
                    get() = channel.isOpen

                override val local: SocketAddress?
                    get() =
                        if (channel.localAddress == null) null
                        else socketAddress(channel.localAddress)

                override val remote: SocketAddress?
                    get() =
                        if (channel.remoteAddress == null) null
                        else socketAddress(channel.remoteAddress)

                override val options: NetworkChannel.Options = options(channel)

                override fun read(destination: MutableBuffer): Int {
                    val buffer = ByteBuffer
                        .allocate(destination.remaining)
                        .clear()
                    val count = channel.read(buffer)
                    TODO("buffer -> destination")
                    return count
                }

                override fun write(source: Buffer): Int {
                    val buffer = ByteBuffer
                        .allocate(source.remaining)
                        .clear()
                    TODO("source -> buffer")
                    return channel.write(buffer)
                }

                override fun bind(address: SocketAddress): SocketChannel {
                    channel.bind(socketAddress(address))
                    return this
                }

                override fun close() {
                    channel.close()
                }
            }
        }

        override fun close() {
            channel.close()
        }
    }
}
