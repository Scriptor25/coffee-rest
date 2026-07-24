package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultStream
import java.io.InputStream

class StreamResultConverter : Converter<InputStream, HTTPResult<*>> {

    override fun from(source: InputStream): HTTPResult<*> {
        return HTTPResultStream(value = source)
    }
}
