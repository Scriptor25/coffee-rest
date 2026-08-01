package dev.scriptor.server.converter

import dev.scriptor.server.Provider

interface Converter<in S : Any, out D : Any> {

    context(provider: Provider)
    fun convert(value: S): D
}
