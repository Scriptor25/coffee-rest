package dev.scriptor.server.converter

import dev.scriptor.server.reflect.Type

data class ConversionStep(
    val src: Type,
    val dst: Type,
    val converter: Converter<Any?, Any?>,
)
