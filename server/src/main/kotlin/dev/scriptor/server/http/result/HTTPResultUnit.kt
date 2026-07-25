package dev.scriptor.server.http.result

import dev.scriptor.server.ParameterList

class HTTPResultUnit : HTTPResult<Unit> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: ParameterList = ParameterList(),
    ) : super(
        statusCode,
        statusText,
        headers,
        null,
        0L,
        0L,
    )
}
