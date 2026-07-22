package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultChannel
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : Converter<ReadableByteChannel, HTTPResult<*>> {

    override fun from(source: ReadableByteChannel): HTTPResult<*> {
        return HTTPResultChannel(statusCode = 200, statusText = "OK", value = source)
    }
}
