package dev.scriptor.server.http

import java.nio.channels.ReadableByteChannel

data class HTTPRequestMessage(
    val method: HTTPMethod,
    val path: String,
    val query: Map<String, MutableList<String>>,
    val protocol: String,
    val headers: Map<String, String>,
    val body: ReadableByteChannel
)
