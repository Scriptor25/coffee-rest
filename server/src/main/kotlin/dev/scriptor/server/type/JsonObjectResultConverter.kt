package dev.scriptor.server.type

import dev.scriptor.server.http.result.HTTPResult
import dev.scriptor.server.http.result.HTTPResultString
import org.json.JSONObject

class JsonObjectResultConverter : IConverter<JSONObject, HTTPResult<*>> {

    override fun from(source: JSONObject): HTTPResult<*> {
        return HTTPResultString(200, "OK", source.toString())
    }
}
