package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class Conversion<in S, out D>(private val path: List<ConversionStep>) {

    context(_: Provider)
    fun convert(value: S): D {
        var current: Any? = value

        for ((_, _, converter) in path) {
            current = converter(current)
        }

        return current as D
    }

    context(_: Provider)
    operator fun invoke(value: S): D {
        return convert(value)
    }
}
