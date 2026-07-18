package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString
import dev.scriptor.server.http.result.HTTPResultVoid

class IntResultConverter : IConverter<Int, HTTPResult<*>> {

    override fun from(source: Int?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultString(200, "OK", source.toString())
    }
}
