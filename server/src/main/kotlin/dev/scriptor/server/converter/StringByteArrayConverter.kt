package dev.scriptor.server.converter

class StringByteArrayConverter : Converter<String, ByteArray> {

    override fun convert(value: String): ByteArray = value.encodeToByteArray()
}
