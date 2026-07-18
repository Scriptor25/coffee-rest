package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString
import dev.scriptor.server.http.result.HTTPResultVoid

class StringResultConverter : IConverter<String, HTTPResult<*>> {

    override fun from(source: String?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultString(200, "OK", source)
    }
}
