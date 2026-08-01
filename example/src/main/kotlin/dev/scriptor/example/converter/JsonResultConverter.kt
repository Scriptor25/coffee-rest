package dev.scriptor.example.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.result.Result
import dev.scriptor.server.result.StringResult
import org.json.JSONObject

class JsonResultConverter : Converter<JSONObject, Result> {

    context(provider: Provider)
    override fun convert(value: JSONObject): Result {
        return StringResult(contentType = "application/json", value = value.toString())
    }
}
