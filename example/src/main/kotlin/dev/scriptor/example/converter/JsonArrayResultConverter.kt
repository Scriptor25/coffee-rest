package dev.scriptor.example.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.result.Result
import dev.scriptor.server.result.StringResult
import org.json.JSONArray

class JsonArrayResultConverter : Converter<JSONArray, Result> {

    context(provider: Provider)
    override fun convert(value: JSONArray): Result =
        StringResult(contentType = "application/json", value = value.toString())
}
