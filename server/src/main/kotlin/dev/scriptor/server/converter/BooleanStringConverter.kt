package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class BooleanStringConverter : Converter<Boolean, String> {

    context(provider: Provider)
    override fun convert(value: Boolean): String = value.toString()
}
