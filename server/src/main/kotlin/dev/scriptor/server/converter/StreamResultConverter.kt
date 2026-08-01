package dev.scriptor.server.converter

import dev.scriptor.server.result.StreamResult
import java.io.InputStream

class StreamResultConverter : ResultConverter<InputStream, StreamResult> {

    override fun convert(value: InputStream) = StreamResult(value = value)
}
