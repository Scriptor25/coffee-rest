package dev.scriptor.example.converter

import dev.scriptor.server.converter.Converter
import org.json.JSONArray

class ArrayJsonConverter : Converter<Array<*>, JSONArray> {

    override fun convert(value: Array<*>): JSONArray {
        return JSONArray(value.asList())
    }
}
