package io.scriptor.server.type

import io.scriptor.server.http.result.HTTPResult
import io.scriptor.server.http.result.HTTPResultChannel
import io.scriptor.server.http.result.HTTPResultVoid
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : IConverter<ReadableByteChannel, HTTPResult<*>> {

    override fun from(source: ReadableByteChannel?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultChannel(200, "OK", source)
    }
}
