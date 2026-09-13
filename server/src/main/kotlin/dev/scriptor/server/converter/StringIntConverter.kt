package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringIntConverter : Converter<String, Int> {

    context(_: Provider?)
    override fun convert(value: String) = value.toInt()
}
