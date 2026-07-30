package dev.scriptor.server.converter

class ConversionPath<in S : Any, out D : Any>(private val path: List<ConversionStep>) {

    constructor() : this(emptyList())

    fun convert(value: S): D {
        var current: Any = value

        for ((_, _, converter) in path) {
            current = converter.convert(current)
        }

        return current as D
    }
}
