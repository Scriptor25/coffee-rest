package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultVoid

class UnitResultConverter : Converter<Unit, HTTPResult<*>> {

    override fun from(source: Unit): HTTPResult<*> {
        return HTTPResultVoid(200, "OK")
    }
}
