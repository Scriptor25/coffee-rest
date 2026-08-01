package dev.scriptor.server.converter

import dev.scriptor.server.result.StringResult

class StringResultConverter : ResultConverter<String, StringResult> {

    override fun convert(value: String) = StringResult(value = value)
}
