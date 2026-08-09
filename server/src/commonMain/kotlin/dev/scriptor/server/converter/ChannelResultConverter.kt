package dev.scriptor.server.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.result.ChannelResult
import dev.scriptor.stdlib.io.ReadableChannel

class ChannelResultConverter : Converter<ReadableChannel, ChannelResult> {

    context(provider: Provider)
    override fun convert(value: ReadableChannel): ChannelResult = ChannelResult(value = value)
}
