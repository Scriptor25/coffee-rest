package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import java.nio.channels.ReadableByteChannel

open class Result {

    val statusCode: Int
    val statusText: String

    val contentType: String

    val headers: ParameterList

    val channel: ReadableByteChannel?

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "*/*",
        headers: ParameterList = ParameterList(),
        channel: ReadableByteChannel?,
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.contentType = contentType
        this.headers = headers
        this.channel = channel
    }
}
