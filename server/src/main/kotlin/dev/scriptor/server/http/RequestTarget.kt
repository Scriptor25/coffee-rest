package dev.scriptor.server.http

sealed interface RequestTarget

/**
 * <method> <path>[?<query>] <protocol>
 */
data class OriginRequestTarget(
    val path: String,
) : RequestTarget {
    override fun toString(): String = path
}

/**
 * <method> * <protocol>
 */
data object AsteriskRequestTarget : RequestTarget {
    override fun toString(): String = "*"
}

/**
 * <method> <host>:<port> <protocol>
 */
data class AuthorityRequestTarget(
    val host: String,
    val port: Int,
) : RequestTarget {
    override fun toString(): String = "$host:$port"
}
