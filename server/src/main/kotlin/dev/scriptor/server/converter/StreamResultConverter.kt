package dev.scriptor.server.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.result.StreamResult
import java.io.InputStream

class StreamResultConverter : ResultConverter<InputStream, StreamResult> {

    context(provider: Provider)
    override fun convert(value: InputStream) = StreamResult(value = value)
}
