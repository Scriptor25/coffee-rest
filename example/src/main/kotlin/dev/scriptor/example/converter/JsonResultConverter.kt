package dev.scriptor.example.converter

import dev.scriptor.server.converter.Converter
import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString
import org.json.JSONObject

class JsonResultConverter : Converter<JSONObject, HTTPResult<*>> {

    override fun from(source: JSONObject): HTTPResult<*> {
        return HTTPResultString(statusCode = 200, statusText = "OK", value = source.toString())
    }
}
