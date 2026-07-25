package dev.scriptor.server.http.result

import dev.scriptor.server.ParameterList
import java.nio.channels.ReadableByteChannel

abstract class HTTPResult<T> {

    val statusCode: Int
    val statusText: String

    val headers: ParameterList

    val channel: ReadableByteChannel?

    val position: Long
    val count: Long

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: ParameterList = ParameterList(),
        channel: ReadableByteChannel? = null,
        position: Long = 0L,
        count: Long = -1L,
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.headers = headers
        this.channel = channel
        this.position = position
        this.count = count
    }
}
