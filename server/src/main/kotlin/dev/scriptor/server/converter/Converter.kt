package dev.scriptor.server.converter

interface Converter<S : Any, D : Any> {

    fun from(source: S): D?
}
