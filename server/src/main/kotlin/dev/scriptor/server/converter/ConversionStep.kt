package dev.scriptor.server.converter

import kotlin.reflect.KType

data class ConversionStep(
    val src: KType,
    val dst: KType,
    val converter: Converter<Any, Any>,
)
