package dev.scriptor.server.converter

class StringDoubleConverter : Converter<String, Double> {

    override fun convert(value: String) = value.toDouble()
}
