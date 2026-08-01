package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class StringLongConverter : Converter<String, Long> {

    context(provider: Provider)
    override fun convert(value: String) = value.toLong()
}
