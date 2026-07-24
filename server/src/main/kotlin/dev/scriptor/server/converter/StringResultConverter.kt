package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString

class StringResultConverter : Converter<String, HTTPResult<*>> {

    override fun convert(value: String): HTTPResult<*> = HTTPResultString(value = value)
}
