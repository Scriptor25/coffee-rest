package dev.scriptor.server.converter

class ConversionPath(private val path: List<ConversionStep>) {

    fun convert(value: Any): Any {
        var current = value

        for ((_, _, converter) in path) {
            current = converter.convert(current)
        }

        return current
    }
}
