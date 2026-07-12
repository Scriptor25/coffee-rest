package io.scriptor.type

import io.scriptor.http.result.HTTPResult
import io.scriptor.http.result.HTTPResultString
import io.scriptor.http.result.HTTPResultVoid
import org.json.JSONObject

class JsonObjectResultConverter : IConverter<JSONObject, HTTPResult<*>> {

    override fun from(source: JSONObject?): HTTPResult<*> {
        if (source == null) {
            return HTTPResultVoid(200, "OK")
        }
        return HTTPResultString(200, "OK", source.toString())
    }
}
