package dev.scriptor.server.http

import dev.scriptor.io.Path
import dev.scriptor.server.ParameterList

data class Request(
    val method: Method,
    val path: Path,
    val query: ParameterList,
    val protocol: String,
    val headers: ParameterList,
    val body: MessageBody,
)
