package dev.scriptor.server.converter

import dev.scriptor.server.Provider

interface Converter<in S, out D> {

    context(provider: Provider)
    fun convert(value: S): D

    context(_: Provider)
    operator fun invoke(value: S): D {
        return convert(value)
    }
}
