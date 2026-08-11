package dev.scriptor.reflect

import kotlin.reflect.KVariance

data class TypeProjection(
    val type: Type,
    val variance: KVariance,
) : Projection {
    override fun toString(): String {
        return buildString {
            when (variance) {
                KVariance.INVARIANT -> {}
                KVariance.OUT -> append("out ")
                KVariance.IN -> append("in ")
            }

            when (type) {
                is ClassReference -> {
                    append(type.id)

                    if (type.arguments.isNotEmpty()) {
                        for ((index, argument) in type.arguments.withIndex()) {
                            if (index > 0) {
                                append(", ")
                            }
                            append(argument)
                        }
                    }
                }

                is TypeParameterReference -> {
                    append(type.parameter.name)
                }
            }

            if (type.nullable) {
                append("?")
            }
        }
    }
}
