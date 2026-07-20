package dev.scriptor.server.http.result

import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

class HTTPResultString : HTTPResult<String> {

    override val count: Long
    override val channel: ReadableByteChannel?

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(statusCode: Int, statusText: String, value: String) : super(statusCode, statusText, value)

    constructor(statusCode: Int, headers: Map<String, String>) : super(statusCode, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>
    ) : super(statusCode, statusText, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>,
        value: String
    ) : super(statusCode, statusText, headers, value)

    init {
        if (value != null) {
            val buf = value.encodeToByteArray()

            count = buf.size.toLong()
            channel = Channels.newChannel(buf.inputStream())
        } else {
            count = 0L
            channel = null
        }
    }
}
