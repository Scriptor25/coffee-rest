package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString

class StringResultConverter : Converter<String, HTTPResult<*>> {

    override fun from(source: String): HTTPResult<*> {
        return HTTPResultString(statusCode = 200, statusText = "OK", value = source)
    }
}
