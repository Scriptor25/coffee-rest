package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultVoid

class VoidResultConverter : IConverter<Void?, HTTPResult<*>> {

    override fun from(source: Void?): HTTPResult<*> {
        return HTTPResultVoid(200, "OK")
    }
}
