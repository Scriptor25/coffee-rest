package dev.scriptor.example.converter

import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString
import org.json.JSONObject

class JsonResultConverter : Converter<JSONObject, HTTPResult<*>> {

    override fun convert(value: JSONObject): HTTPResult<*> {
        return HTTPResultString(contentType = "application/json", value = value.toString())
    }
}
