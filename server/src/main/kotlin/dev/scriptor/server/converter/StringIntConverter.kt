package dev.scriptor.server.converter

class StringIntConverter : Converter<String, Int> {

    override fun from(source: String): Int? {
        return source.toIntOrNull()
    }
}
