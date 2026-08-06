package dev.scriptor.server.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.result.StringResult

class StringResultConverter : Converter<String, StringResult> {

    context(provider: Provider)
    override fun convert(value: String) = StringResult(value = value)
}
