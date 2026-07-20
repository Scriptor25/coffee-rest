package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString

class IntResultConverter : IConverter<Int, HTTPResult<*>> {

    override fun from(source: Int): HTTPResult<*> {
        return HTTPResultString(statusCode = 200, statusText = "OK", value = source.toString())
    }
}
