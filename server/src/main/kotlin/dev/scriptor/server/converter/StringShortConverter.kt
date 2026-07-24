package dev.scriptor.server.converter

class StringShortConverter : Converter<String, Short> {

    override fun convert(value: String): Short = value.toShort()
}
