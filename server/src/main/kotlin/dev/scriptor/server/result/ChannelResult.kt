package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import java.nio.channels.ReadableByteChannel

class ChannelResult : Result {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "application/octet-stream",
        headers: ParameterList = ParameterList(),
        value: ReadableByteChannel,
    ) : super(
        statusCode,
        statusText,
        contentType,
        headers,
        value,
    )
}
