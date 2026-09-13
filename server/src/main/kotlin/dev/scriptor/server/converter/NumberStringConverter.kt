package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class NumberStringConverter : Converter<Number, String> {

    context(_: Provider?)
    override fun convert(value: Number) = value.toString()
}
