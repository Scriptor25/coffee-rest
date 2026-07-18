package dev.scriptor.server.http.result

import java.io.InputStream

abstract class HTTPResult<T> {

    val statusCode: Int
    val statusText: String

    val headers: Map<String, String>

    private val body: T?

    abstract val size: Int
    abstract val stream: InputStream?

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
        this.body = null
    }

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>,
        body: T
    ) {
        this.statusCode = statusCode
        this.statusText = statusText
        this.headers = HashMap(headers)
        this.body = body
    }

    override fun toString(): String {
        return "Result( statusCode=%d, statusText=%s, value=%s )".format(statusCode, statusText, body)
    }

    fun getBody(): T {
        checkNotNull(body)
        return body
    }
}
