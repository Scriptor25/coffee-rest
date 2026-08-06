package dev.scriptor.server.reflect

data class Callable(
    val returnType: Type,
    val parameters: List<Parameter>,
    val call: (Array<Any?>) -> Any?,
)
