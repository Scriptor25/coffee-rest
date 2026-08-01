package dev.scriptor.server.converter

import dev.scriptor.server.result.ChannelResult
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : ResultConverter<ReadableByteChannel, ChannelResult> {

    override fun convert(value: ReadableByteChannel) = ChannelResult(value = value)
}
