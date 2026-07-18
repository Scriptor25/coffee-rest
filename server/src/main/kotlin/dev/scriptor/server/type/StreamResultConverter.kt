package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultStream
import dev.scriptor.server.http.result.HTTPResultVoid
import java.io.InputStream

class StreamResultConverter : IConverter<InputStream, HTTPResult<*>> {

    override fun from(source: InputStream?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultStream(200, "OK", source)
    }
}
