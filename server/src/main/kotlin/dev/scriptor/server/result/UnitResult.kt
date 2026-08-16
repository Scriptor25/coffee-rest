package dev.scriptor.server.result

import dev.scriptor.server.ParameterList

class UnitResult : Result {

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
    )
}
