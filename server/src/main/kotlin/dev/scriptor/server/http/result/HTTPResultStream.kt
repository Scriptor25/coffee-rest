package dev.scriptor.server.http.result

import dev.scriptor.server.ParameterList
import java.io.InputStream
import java.nio.channels.Channels

class HTTPResultStream : HTTPResult<InputStream> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: ParameterList = ParameterList(),
        value: InputStream? = null,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        headers,
        if (value != null)
            Channels.newChannel(value)
        else null,
        0L,
        count,
    )
}
