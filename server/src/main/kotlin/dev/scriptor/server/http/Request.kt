package dev.scriptor.server.http

import dev.scriptor.server.ParameterList

data class Request(
    val method: Method,
    val target: RequestTarget,
    val protocol: Version,
    val query: ParameterList,
    val headers: ParameterList,
    val body: MessageBody,
) {
    override fun toString(): String = "$method $target $protocol"
}
