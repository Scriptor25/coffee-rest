package dev.scriptor.server.converter

import dev.scriptor.io.channels.ReadableByteChannel
import dev.scriptor.server.Provider
import dev.scriptor.server.result.ChannelResult

class ChannelResultConverter : Converter<ReadableByteChannel, ChannelResult> {

    context(provider: Provider)
    override fun convert(value: ReadableByteChannel): ChannelResult = ChannelResult(value = value)
}
