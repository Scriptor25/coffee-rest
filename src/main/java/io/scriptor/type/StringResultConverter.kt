package io.scriptor.type

import io.scriptor.http.result.HTTPResult
import io.scriptor.http.result.HTTPResultString
import io.scriptor.http.result.HTTPResultVoid

class StringResultConverter : IConverter<String, HTTPResult<*>> {

    override fun from(source: String?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultString(200, "OK", source)
    }
}
