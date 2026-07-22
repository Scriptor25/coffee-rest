package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString

class IntResultConverter : Converter<Int, HTTPResult<*>> {

    override fun from(source: Int): HTTPResult<*> {
        return HTTPResultString(statusCode = 200, statusText = "OK", value = source.toString())
    }
}
