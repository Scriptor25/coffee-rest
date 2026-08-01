package dev.scriptor.server.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.result.ChannelResult
import java.nio.channels.ReadableByteChannel

class ChannelResultConverter : ResultConverter<ReadableByteChannel, ChannelResult> {

    context(provider: Provider)
    override fun convert(value: ReadableByteChannel) = ChannelResult(value = value)
}
