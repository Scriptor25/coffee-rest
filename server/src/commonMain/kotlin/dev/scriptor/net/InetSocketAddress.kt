package dev.scriptor.net

interface InetSocketAddress : SocketAddress {

    val address: InetAddress

    val hostname: String
    val port: Int

    val unresolved: Boolean
}

fun InetSocketAddress(port: Int): InetSocketAddress {
    TODO()
}

fun InetSocketAddress(hostname: String, port: Int): InetSocketAddress {
    TODO()
}

fun InetSocketAddress(address: InetAddress, port: Int): InetSocketAddress {
    TODO()
}
