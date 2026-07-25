package dev.scriptor.server.http

import dev.scriptor.server.ParameterList

data class HTTPRequestMessage(
    val method: HTTPMethod,
    val path: String,
    val query: ParameterList,
    val protocol: String,
    val headers: ParameterList,
    val body: HTTPMessageBody
)
