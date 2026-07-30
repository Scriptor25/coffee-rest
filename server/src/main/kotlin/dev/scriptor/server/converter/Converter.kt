package dev.scriptor.server.converter

interface Converter<in S : Any, out D : Any> {

    fun convert(value: S): D
}
