package dev.scriptor.server.http.result

import java.nio.channels.ReadableByteChannel

class HTTPResultChannel : HTTPResult<ReadableByteChannel> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: Map<String, String> = emptyMap(),
        value: ReadableByteChannel? = null,
        position: Long = 0L,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        headers,
        position,
        count,
        value,
    )
}
