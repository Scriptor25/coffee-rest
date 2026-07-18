package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultVoid

class UnitResultConverter : IConverter<Unit, HTTPResult<*>> {

    override fun from(source: Unit): HTTPResult<*> {
        return HTTPResultVoid(200, "OK")
    }
}
