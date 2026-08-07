package dev.scriptor.server.reflect

data class TypeParameter(
    val upperBounds: List<Type>,
) : Classifier
