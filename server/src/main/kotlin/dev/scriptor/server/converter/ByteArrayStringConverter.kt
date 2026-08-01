package dev.scriptor.server.converter

class ByteArrayStringConverter : Converter<ByteArray, String> {

    override fun convert(value: ByteArray) = value.decodeToString()
}
