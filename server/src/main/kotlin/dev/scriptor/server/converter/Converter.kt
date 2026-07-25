package dev.scriptor.server.converter

interface Converter<S : Any, D : Any> {

    val name: String?
        get() = this::class.qualifiedName

    fun convert(value: S): D
}
