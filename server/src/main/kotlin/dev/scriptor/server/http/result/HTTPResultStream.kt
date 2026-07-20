package dev.scriptor.server.http.result

import java.io.InputStream
import java.nio.channels.Channels

class HTTPResultStream : HTTPResult<InputStream> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: Map<String, String> = emptyMap(),
        value: InputStream? = null,
        count: Long = -1L,
    ) : super(
        statusCode,
        statusText,
        headers,
        0L,
        count,
        if (value != null) Channels.newChannel(value)
        else null,
    )
}
