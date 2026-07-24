package dev.scriptor.server.converter

class StringIntConverter : Converter<String, Int> {

    override fun convert(value: String): Int = value.toInt()
}
