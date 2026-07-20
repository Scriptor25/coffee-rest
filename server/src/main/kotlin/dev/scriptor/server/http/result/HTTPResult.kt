package dev.scriptor.server.http.result

import java.nio.channels.ReadableByteChannel

abstract class HTTPResult<T> {

    val statusCode: Int
    val statusText: String

    val headers: Map<String, String>

    val position: Long
    val count: Long

    val channel: ReadableByteChannel?

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: Map<String, String> = emptyMap(),
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
