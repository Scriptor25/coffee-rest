package dev.scriptor.server.http.result

import java.nio.channels.ReadableByteChannel

abstract class HTTPResult<T> {

    val statusCode: Int
    val statusText: String

    val headers: Map<String, String>

    val value: T?

    abstract val count: Long
    abstract val channel: ReadableByteChannel?

    constructor(statusCode: Int, statusText: String, body: T) : this(
        statusCode,
        statusText,
        HashMap(),
        body
    )

    constructor(statusCode: Int, headers: Map<String, String>) : this(statusCode, "", headers)

    constructor(
        statusCode: Int,
        statusText: String = "",
        headers: Map<String, String> = HashMap()
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.headers = HashMap(headers)
        this.value = null
    }

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>,
        value: T
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.headers = HashMap(headers)
        this.value = value
    }
}
