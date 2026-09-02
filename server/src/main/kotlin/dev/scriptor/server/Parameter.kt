package dev.scriptor.server

import dev.scriptor.reflect.Type

enum class ParameterKind {
    CONTEXT,
    VALUE,
}

data class Parameter(
    val index: Int,
    val name: String,
    val type: Type,
    val kind: ParameterKind,
    val optional: Boolean,
    val varargs: Boolean,
    val annotation: ParameterAnnotation,
)

sealed interface ParameterAnnotation {

    data object None : ParameterAnnotation

    data class Path(val name: String?) : ParameterAnnotation
    data class Query(val name: String?) : ParameterAnnotation
    data class Header(val name: String?) : ParameterAnnotation

    data object Body : ParameterAnnotation
}
