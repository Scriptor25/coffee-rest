package dev.scriptor.server.converter

class IntStringConverter : Converter<Int, String> {

    override fun from(source: Int): String {
        return source.toString()
    }
}
