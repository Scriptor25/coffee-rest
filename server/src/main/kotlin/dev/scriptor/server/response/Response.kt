package dev.scriptor.server.response

import dev.scriptor.server.ParameterList
import dev.scriptor.server.http.MessageBody
import dev.scriptor.server.http.Version

data class Response(
    val protocol: Version,
    val statusCode: Int,
    val statusText: String,
    val headers: ParameterList,
    val body: MessageBody?,
)
