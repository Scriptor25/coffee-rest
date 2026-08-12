package dev.scriptor.reflect

import kotlin.reflect.KVariance

data class TypeParameter(
    val id: TypeParameterId,
    val variance: KVariance,
    val upperbounds: Set<Type>,
) {
    class Builder(val id: TypeParameterId) {
        lateinit var variance: KVariance
        val upperbounds = mutableSetOf<Type>()

        fun upperbound(id: TypeParameterId, cls: ClassId, block: TypeParameterReference.Builder.() -> Unit = {}) {
            upperbounds += TypeParameterReference.Builder(cls, id).apply(block).build()
        }

        fun upperbound(id: ClassId, block: ClassReference.Builder.() -> Unit = {}) {
            upperbounds += ClassReference.Builder(id).apply(block).build()
        }

        fun build(): TypeParameter = TypeParameter(id, variance, upperbounds)
    }

    companion object {
        fun create(id: TypeParameterId, block: Builder.() -> Unit): TypeParameter {
            return Builder(id).apply(block).build()
        }
    }

    override fun toString(): String = buildString {
        when (variance) {
            KVariance.INVARIANT -> {}
            KVariance.OUT -> append("out ")
            KVariance.IN -> append("in ")
        }

        append(id)

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
