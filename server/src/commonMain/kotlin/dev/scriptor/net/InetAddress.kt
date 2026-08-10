package dev.scriptor.net

interface InetAddress {
    val address: ByteArray

    val hostAddress: String
    val hostname: String
    val canonicalHostname: String

    val wildcard: Boolean
    val linkLocal: Boolean
    val loopback: Boolean
    val siteLocal: Boolean

    val multicast: Boolean
    val multicastGlobal: Boolean
    val multicastLinkLocal: Boolean
    val multicastNodeLocal: Boolean
    val multicastOrgLocal: Boolean
    val multicastSiteLocal: Boolean

    fun isReachable(timeout: Long): Boolean
    fun isReachable(timeout: Long, interf: NetworkInterface, ttl: Long): Boolean
}
