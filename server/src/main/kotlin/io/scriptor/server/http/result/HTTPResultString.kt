package io.scriptor.server.http.result

import java.io.ByteArrayInputStream
import java.io.InputStream

class HTTPResultString : HTTPResult<String> {

    override val size: Int
    override val stream: InputStream

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(statusCode: Int, statusText: String, value: String) : super(statusCode, statusText, value)

    constructor(statusCode: Int, headers: Map<String, String>) : super(statusCode, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>
    ) : super(statusCode, statusText, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>,
        value: String
    ) : super(statusCode, statusText, headers, value)

    init {
        val buf = getBody().toByteArray()

        size = buf.size
        stream = ByteArrayInputStream(buf)
    }
}
