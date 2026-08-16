package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import dev.scriptor.server.RangeReadableByteChannel
import java.io.InputStream
import java.nio.channels.Channels

class StreamResult : Result {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "application/octet-stream",
        headers: ParameterList = ParameterList(),
        value: InputStream,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        contentType,
        headers,
        when {
            count < 0L -> Channels.newChannel(value)
            else -> RangeReadableByteChannel(Channels.newChannel(value), 0L until count)
        },
    )
}
