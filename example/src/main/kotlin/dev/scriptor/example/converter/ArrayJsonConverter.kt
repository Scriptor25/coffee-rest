package dev.scriptor.example.converter

import dev.scriptor.server.converter.Converter
import org.json.JSONArray

class ArrayJsonConverter : Converter<Array<Any>, JSONArray> {

    override fun convert(value: Array<Any>): JSONArray {
        return JSONArray(value)
    }
}
