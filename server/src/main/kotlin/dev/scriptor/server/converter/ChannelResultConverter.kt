package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultChannel
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : Converter<ReadableByteChannel, HTTPResult<*>> {

    override fun convert(value: ReadableByteChannel): HTTPResult<*> = HTTPResultChannel(value = value)
}
