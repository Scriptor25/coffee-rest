package dev.scriptor.reflect

import kotlin.reflect.KVariance

data class TypeParameter(
    val name: String,
    val variance: KVariance,
    val upperbounds: List<Type>,
) {
    override fun toString(): String {
        return buildString {
            when (variance) {
                KVariance.INVARIANT -> {}
                KVariance.OUT -> append("out ")
                KVariance.IN -> append("in ")
            }

            append(name)

            if (upperbounds.isNotEmpty()) {
                append(" : ")

                for ((index, type) in upperbounds.withIndex()) {
                    if (index > 0) {
                        append(", ")
                    }
                    append(type)
                }
            }
        }
    }
}
