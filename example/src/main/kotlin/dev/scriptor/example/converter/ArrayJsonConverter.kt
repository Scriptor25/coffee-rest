package dev.scriptor.example.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.converter.Converter
import org.json.JSONArray

class ArrayJsonConverter : Converter<Array<*>, JSONArray> {

    context(provider: Provider)
    override fun convert(value: Array<*>): JSONArray = JSONArray(value.asList())
}
