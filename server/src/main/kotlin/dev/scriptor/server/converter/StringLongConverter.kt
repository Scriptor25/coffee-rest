package dev.scriptor.server.converter

class StringLongConverter : Converter<String, Long> {

    override fun convert(value: String): Long = value.toLong()
}
