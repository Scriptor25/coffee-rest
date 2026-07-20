package dev.scriptor.server.http.result

class HTTPResultVoid : HTTPResult<Void> {

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: Map<String, String> = emptyMap(),
    ) : super(statusCode, statusText, headers, 0L, 0L, null)
}
