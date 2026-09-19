package dev.scriptor.server.request

import dev.scriptor.server.ParameterList
import dev.scriptor.server.http.MessageBody
import dev.scriptor.server.http.Method
import dev.scriptor.server.http.Version

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
