package dev.scriptor.server.http

import dev.scriptor.server.ParameterList

data class Response(
    val protocol: Version,
    val statusCode: Int,
    val statusText: String,
    val headers: ParameterList,
    val body: MessageBody?,
)
