package dev.scriptor.server.converter

class StringFloatConverter : Converter<String, Float> {

    override fun convert(value: String): Float = value.toFloat()
}
