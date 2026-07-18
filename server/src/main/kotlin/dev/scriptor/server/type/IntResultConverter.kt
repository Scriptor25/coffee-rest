package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString

class IntResultConverter : IConverter<Int, HTTPResult<*>> {

    override fun from(source: Int): HTTPResult<*> {
        return HTTPResultString(200, "OK", source.toString())
    }
}
