package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class ByteArrayStringConverter : Converter<ByteArray, String> {

    context(provider: Provider)
    override fun convert(value: ByteArray) = value.decodeToString()
}
