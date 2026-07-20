package dev.scriptor.server.http.result

import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel

class HTTPResultChannel : HTTPResult<ReadableByteChannel> {

    override val count: Long =
        if (value is FileChannel) value.size()
        else -1L
    override val channel: ReadableByteChannel? = value

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(
        statusCode: Int,
        statusText: String,
        value: ReadableByteChannel
    ) : super(statusCode, statusText, value)

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
        value: ReadableByteChannel
    ) : super(statusCode, statusText, headers, value)
}
