package io.scriptor.server.type

import io.scriptor.server.http.result.HTTPResult
import io.scriptor.server.http.result.HTTPResultStream
import io.scriptor.server.http.result.HTTPResultVoid
import java.io.InputStream

class StreamResultConverter : IConverter<InputStream, HTTPResult<*>> {

    override fun from(source: InputStream?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultStream(200, "OK", source)
    }
}
