package dev.scriptor.server.reflect

data class TypeProjection(
    val variance: Variance,
    val type: Type,
) : Projection
