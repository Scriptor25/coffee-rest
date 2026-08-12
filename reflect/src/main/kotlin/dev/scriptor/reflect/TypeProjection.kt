package dev.scriptor.reflect

import kotlin.reflect.KVariance

data class TypeProjection(
    val variance: KVariance,
    val type: Type,
) : Projection {
    class Builder {
        lateinit var variance: KVariance
        lateinit var type: Type

        fun type(
            cls: ClassId,
            id: TypeParameterId,
            block: TypeParameterReference.Builder.() -> Unit = {},
        ): TypeParameterReference {
            val reference = TypeParameterReference.Builder(cls, id).apply(block).build()
            type = reference
            return reference
        }

        fun build(): TypeProjection = TypeProjection(variance, type)
    }

    companion object {
        fun create(block: Builder.() -> Unit): TypeProjection {
            return Builder().apply(block).build()
        }
    }

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
                        append("<")
                        for ((index, argument) in type.arguments.withIndex()) {
                            if (index > 0) {
                                append(", ")
                            }
                            append(argument)
                        }
                        append(">")
                    }
                }

                is TypeParameterReference -> {
                    append(type)
                }
            }

            if (type.nullable) {
                append("?")
            }
        }
    }
}
