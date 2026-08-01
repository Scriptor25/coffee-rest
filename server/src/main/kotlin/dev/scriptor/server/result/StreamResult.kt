package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import java.io.InputStream
import java.nio.channels.Channels

class StreamResult : Result {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "application/octet-stream",
        headers: ParameterList = ParameterList(),
        value: InputStream? = null,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        contentType,
        headers,
        if (value != null)
            Channels.newChannel(value)
        else null,
        0L,
        count,
    )
}
