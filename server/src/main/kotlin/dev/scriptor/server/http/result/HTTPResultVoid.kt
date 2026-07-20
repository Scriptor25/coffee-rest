package dev.scriptor.server.http.result

import java.nio.channels.ReadableByteChannel

class HTTPResultVoid : HTTPResult<Void> {

    override val count: Long = 0L
    override val channel: ReadableByteChannel? = null

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>
    ) : super(statusCode, statusText, headers)
}
