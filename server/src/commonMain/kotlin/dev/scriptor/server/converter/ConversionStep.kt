package dev.scriptor.server.converter

import dev.scriptor.reflect.Type
import dev.scriptor.server.ConverterFn

data class ConversionStep(
    val src: Type,
    val dst: Type,
    val converter: ConverterFn,
)
