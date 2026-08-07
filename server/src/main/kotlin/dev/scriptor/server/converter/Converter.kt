package dev.scriptor.server.converter

import dev.scriptor.server.Provider

interface Converter<in S, out D> {

    context(provider: Provider)
    fun convert(value: S): D
}
