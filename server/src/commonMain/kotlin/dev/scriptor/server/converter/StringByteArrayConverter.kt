package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringByteArrayConverter : Converter<String, ByteArray> {

    context(provider: Provider)
    override fun convert(value: String) = value.encodeToByteArray()
}
