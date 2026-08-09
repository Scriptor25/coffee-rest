package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import dev.scriptor.stdlib.io.ReadableChannel

open class Result {

    val statusCode: Int
    val statusText: String

    val contentType: String

    val headers: ParameterList

    val channel: ReadableChannel?

    val position: Long
    val count: Long

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "*/*",
        headers: ParameterList = ParameterList(),
        channel: ReadableChannel? = null,
        position: Long = 0L,
        count: Long = -1L,
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.contentType = contentType
        this.headers = headers
        this.channel = channel
        this.position = position
        this.count = count
    }
}
