package dev.scriptor.server.http.result

import dev.scriptor.server.http.ParameterList
import java.nio.channels.ReadableByteChannel

abstract class HTTPResult<T> {

    val statusCode: Int
    val statusText: String

    val headers: ParameterList

    val position: Long
    val count: Long

    val channel: ReadableByteChannel?

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: ParameterList = ParameterList(),
        position: Long = 0L,
        count: Long = -1L,
        channel: ReadableByteChannel? = null,
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.headers = headers
        this.position = position
        this.count = count
        this.channel = channel
    }
}
