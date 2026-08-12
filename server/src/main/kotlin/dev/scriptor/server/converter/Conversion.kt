package dev.scriptor.server.converter

import dev.scriptor.server.Provider

class Conversion(private val path: List<ConversionStep>) {

    context(_: Provider)
    fun convert(value: Any?): Any? {
        var current: Any? = value

        for ((_, _, converter) in path) {
            current = converter(current)
        }

        return current
    }

    context(_: Provider)
    operator fun invoke(value: Any?): Any? = convert(value)
}
