package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringDoubleConverter : Converter<String, Double> {

    context(provider: Provider)
    override fun convert(value: String) = value.toDouble()
}
