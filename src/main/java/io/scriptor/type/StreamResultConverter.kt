package io.scriptor.type

import io.scriptor.http.result.HTTPResult
import io.scriptor.http.result.HTTPResultStream
import io.scriptor.http.result.HTTPResultVoid
import java.io.InputStream

class StreamResultConverter : IConverter<InputStream, HTTPResult<*>> {

    override fun from(source: InputStream?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultStream(200, "OK", source)
    }
}
