package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringByteConverter : Converter<String, Byte> {

    context(_: Provider?)
    override fun convert(value: String) = value.toByte()
}
