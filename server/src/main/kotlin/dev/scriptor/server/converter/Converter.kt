package dev.scriptor.server.converter

interface Converter<S : Any, D : Any> {

    fun convert(value: S): D
}
