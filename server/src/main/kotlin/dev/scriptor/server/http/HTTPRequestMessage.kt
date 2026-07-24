package dev.scriptor.server.http

import java.nio.channels.ReadableByteChannel

data class HTTPRequestMessage(
    val method: HTTPMethod,
    val path: String,
    val query: ParameterList,
    val protocol: String,
    val headers: ParameterList,
    val body: ReadableByteChannel
)
