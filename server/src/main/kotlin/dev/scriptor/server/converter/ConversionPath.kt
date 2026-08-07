package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class ConversionPath<in S, out D>(private val path: List<ConversionStep>) {

    context(_: Provider)
    fun convert(value: S): D {
        var current: Any? = value

        for ((_, _, converter) in path) {
            current = converter.convert(current)
        }

        return current as D
    }

    context(_: Provider)
    operator fun invoke(value: S): D = convert(value)
}
