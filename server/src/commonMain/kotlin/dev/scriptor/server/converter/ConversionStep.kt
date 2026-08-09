package dev.scriptor.server.converter

import dev.scriptor.reflect.Type

data class ConversionStep(
    val src: Type,
    val dst: Type,
    val converter: (Any?) -> Any?,
)
