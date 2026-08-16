package dev.scriptor.server.http

import dev.scriptor.server.ParameterList

data class Request(
    val method: Method,
    val path: String,
    val query: ParameterList,
    val protocol: Version,
    val headers: ParameterList,
    val body: MessageBody
)
