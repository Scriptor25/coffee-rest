package dev.scriptor.net

interface NetworkInterface {
    val index: Int
    val name: String
    val displayName: String

    val parent: NetworkInterface?
    val children: List<NetworkInterface>

    val hardwareAddress: ByteArray
    val inetAddresses: List<InetAddress>
    val interfaceAddresses: List<InterfaceAddress>

    val mtu: Int
    val loopback: Boolean
    val pointToPoint: Boolean
    val up: Boolean
    val virtual: Boolean
    val multicast: Boolean
}
