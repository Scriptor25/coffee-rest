package io.scriptor.type

import io.scriptor.http.result.HTTPResult
import io.scriptor.http.result.HTTPResultString
import io.scriptor.http.result.HTTPResultVoid

class IntResultConverter : IConverter<Int, HTTPResult<*>> {

    override fun from(source: Int?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultString(200, "OK", source.toString())
    }
}
