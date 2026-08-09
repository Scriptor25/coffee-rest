package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import dev.scriptor.stdlib.io.ReadableChannel

class ChannelResult : Result {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "application/octet-stream",
        headers: ParameterList = ParameterList(),
        value: ReadableChannel? = null,
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
