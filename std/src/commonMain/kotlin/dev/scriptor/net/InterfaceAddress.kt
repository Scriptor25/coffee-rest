package dev.scriptor.net

interface InterfaceAddress {
    val address: InetAddress
    val broadcast: InetAddress
    val networkPrefixLength: Short
}
