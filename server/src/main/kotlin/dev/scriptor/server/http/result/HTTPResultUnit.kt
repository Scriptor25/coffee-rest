package dev.scriptor.server.http.result

import dev.scriptor.server.ParameterList

class HTTPResultUnit : HTTPResult<Unit> {

    constructor(
        statusCode: Int = 204,
        statusText: String = "No Content",
        headers: ParameterList = ParameterList(),
    ) : super(
        statusCode,
        statusText,
        "*/*",
        headers,
        null,
        0L,
        0L,
    )
}
