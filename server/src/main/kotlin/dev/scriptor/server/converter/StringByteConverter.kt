package dev.scriptor.server.converter

class StringByteConverter : Converter<String, Byte> {

    override fun convert(value: String) = value.toByte()
}
