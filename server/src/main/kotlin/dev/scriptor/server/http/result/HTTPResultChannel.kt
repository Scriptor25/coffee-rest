package dev.scriptor.server.http.result

import dev.scriptor.server.ParameterList
import java.nio.channels.ReadableByteChannel

class HTTPResultChannel : HTTPResult<ReadableByteChannel> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "application/octet-stream",
        headers: ParameterList = ParameterList(),
        value: ReadableByteChannel? = null,
        position: Long = 0L,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        contentType,
        headers,
        value,
        position,
        count,
    )
}
