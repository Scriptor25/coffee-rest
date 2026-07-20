package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString

class StringResultConverter : IConverter<String, HTTPResult<*>> {

    override fun from(source: String): HTTPResult<*> {
        return HTTPResultString(statusCode = 200, statusText = "OK", value = source)
    }
}
