package dev.scriptor.server.http.result

import dev.scriptor.server.ParameterList
import java.nio.channels.ReadableByteChannel

class HTTPResultChannel : HTTPResult<ReadableByteChannel> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: ParameterList = ParameterList(),
        value: ReadableByteChannel? = null,
        position: Long = 0L,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        headers,
        value,
        position,
        count,
    )
}
