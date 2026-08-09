package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringShortConverter : Converter<String, Short> {

    context(provider: Provider)
    override fun convert(value: String) = value.toShort()
}
