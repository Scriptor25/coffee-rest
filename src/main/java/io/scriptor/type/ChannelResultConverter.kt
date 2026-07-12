package io.scriptor.type

import io.scriptor.http.result.HTTPResult
import io.scriptor.http.result.HTTPResultChannel
import io.scriptor.http.result.HTTPResultVoid
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : IConverter<ReadableByteChannel, HTTPResult<*>> {

    override fun from(source: ReadableByteChannel?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultChannel(200, "OK", source)
    }
}
