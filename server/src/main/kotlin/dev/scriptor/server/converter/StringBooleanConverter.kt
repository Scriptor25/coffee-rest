package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringBooleanConverter : Converter<String, Boolean> {

    context(provider: Provider)
    override fun convert(value: String): Boolean = value.toBooleanStrict()
}
