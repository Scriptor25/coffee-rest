package dev.scriptor.server.converter

class NumberStringConverter : Converter<Number, String> {

    override fun convert(value: Number): String = value.toString()
}
