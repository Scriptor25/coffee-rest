package dev.scriptor.example.converter

import dev.scriptor.reflect.getClass
import dev.scriptor.server.Provider
import dev.scriptor.server.converter.Converter
import org.json.JSONArray
import org.json.JSONObject

class ArrayJsonConverter : Converter<Array<*>, JSONArray> {

    context(provider: Provider)
    override fun convert(value: Array<*>): JSONArray = JSONArray(value.map {
        when (it) {
            null -> JSONObject.NULL

            is String, is Number, is Boolean, is Enum<*> -> it

            is Iterable<*> -> {
                val src = getClass(it::class).starProjectedType
                val dst = getClass(JSONArray::class).starProjectedType
                provider(src, dst, it)
            }

            else -> {
                val src = getClass(it::class).starProjectedType
                val dst = getClass(JSONObject::class).starProjectedType
                provider(src, dst, it)
            }
        }
    })
}
