package io.scriptor.server.type

import io.scriptor.server.http.result.HTTPResult
import io.scriptor.server.http.result.HTTPResultString
import io.scriptor.server.http.result.HTTPResultVoid
import org.json.JSONObject

class JsonObjectResultConverter : IConverter<JSONObject, HTTPResult<*>> {

    override fun from(source: JSONObject?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultString(200, "OK", source.toString())
    }
}
