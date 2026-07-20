package dev.scriptor.server.http.result

import java.io.InputStream
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

class HTTPResultStream : HTTPResult<InputStream> {

    override val count: Long = -1L
    override val channel: ReadableByteChannel? =
        if (value != null) Channels.newChannel(value)
        else null

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(statusCode: Int, statusText: String, value: InputStream) : super(statusCode, statusText, value)

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
        value: InputStream
    ) : super(statusCode, statusText, headers, value)
}
