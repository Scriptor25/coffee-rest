package io.scriptor.http.result

class HTTPResultVoid : HTTPResult<Void> {

    override val size = 0
    override val stream = null

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>
    ) : super(statusCode, statusText, headers)
}
