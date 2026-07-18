package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultChannel
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : IConverter<ReadableByteChannel, HTTPResult<*>> {

    override fun from(source: ReadableByteChannel): HTTPResult<*> {
        return HTTPResultChannel(200, "OK", source)
    }
}
