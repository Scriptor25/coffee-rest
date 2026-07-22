package dev.scriptor.server.converter

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString
import org.json.JSONObject

class JsonObjectResultConverter : Converter<JSONObject, HTTPResult<*>> {

    override fun from(source: JSONObject): HTTPResult<*> {
        return HTTPResultString(statusCode = 200, statusText = "OK", value = source.toString())
    }
}
