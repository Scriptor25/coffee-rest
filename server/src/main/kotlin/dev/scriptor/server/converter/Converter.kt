package dev.scriptor.server.converter

import dev.scriptor.server.Provider

typealias ConverterFn<S, D> = context(Provider) (S) -> D
typealias AnyConverterFn = ConverterFn<Any?, Any?>

interface Converter<in S, out D> {

    context(provider: Provider)
    fun convert(value: S): D

    context(_: Provider)
    operator fun invoke(value: S): D = convert(value)
}
