package dev.scriptor.example.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.convert
import dev.scriptor.server.converter.Converter
import org.json.JSONArray
import org.json.JSONObject

class ArrayJsonConverter : Converter<Array<Any>, JSONArray> {

    context(provider: Provider)
    override fun convert(value: Array<Any>): JSONArray {
        val converter = provider.convert<Any, JSONObject>()!!

        val json = JSONArray()
        for (entry in value) {
            json.put(converter.convert(entry))
        }

        return json
    }
}
