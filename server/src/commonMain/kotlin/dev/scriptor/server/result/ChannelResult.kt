package dev.scriptor.server.result

import dev.scriptor.io.channels.ReadableByteChannel
import dev.scriptor.server.ParameterList

class ChannelResult : Result {

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
