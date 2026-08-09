package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringFloatConverter : Converter<String, Float> {

    context(provider: Provider)
    override fun convert(value: String) = value.toFloat()
}
