package dev.scriptor.reflect

import kotlin.reflect.KVariance

data class Class(
    val id: ClassId,
    val parameters: List<TypeParameter>,
    val supertypes: Set<ClassReference>,
) {
    class Builder(val id: ClassId) {
        val parameters = mutableListOf<TypeParameter>()
        val supertypes = mutableSetOf<ClassReference>()

        fun parameter(id: TypeParameterId, block: TypeParameter.Builder.() -> Unit = {}): TypeParameter {
            val parameter = TypeParameter.Builder(id).apply(block).build()
            parameters += parameter
            return parameter
        }

        fun supertype(id: ClassId, block: ClassReference.Builder.() -> Unit = {}): ClassReference {
            val supertype = ClassReference.Builder(id).apply(block).build()
            supertypes += supertype
            return supertype
        }

        fun build(): Class = Class(id, parameters, supertypes)
    }

    companion object {
        fun create(id: ClassId, block: Builder.() -> Unit = {}): Class {
            return Builder(id).apply(block).build()
        }

        private fun createAny(id: ClassId, block: Builder.() -> Unit = {}): Class {
            return create(id) {
                supertype(ClassId.Any)

                this.apply(block)
            }
        }

        private fun createComparable(id: ClassId, block: Builder.() -> Unit = {}): Class {
            return createAny(id) {
                supertype(ClassId.Comparable) {
                    argument {
                        variance = KVariance.INVARIANT
                        type = ClassReference.create(id)
                    }
                }

                this.apply(block)
            }
        }

        private fun createNumber(id: ClassId, block: Builder.() -> Unit = {}): Class {
            return createComparable(id) {
                supertype(ClassId.Number)

                this.apply(block)
            }
        }

        /** Any */
        val Any = create(ClassId.Any)

        /** Nothing */
        val Nothing = create(ClassId.Nothing)

        /** Unit */
        val Unit = createAny(ClassId.Unit)

        /** Boolean : Comparable<Boolean> */
        val Boolean = createComparable(ClassId.Boolean)

        /** Number */
        val Number = createAny(ClassId.Number)

        /** Byte : Comparable<Byte> */
        val Byte = createNumber(ClassId.Byte)

        /** Short : Comparable<Short> */
        val Short = createNumber(ClassId.Short)

        /** Int : Comparable<Int> */
        val Int = createNumber(ClassId.Int)

        /** Long : Comparable<Long> */
        val Long = createNumber(ClassId.Long)

        /** Float : Comparable<Float> */
        val Float = createNumber(ClassId.Float)

        /** Double : Comparable<Double> */
        val Double = createNumber(ClassId.Double)

        /** Char : Comparable<Char> */
        val Char = createComparable(ClassId.Char)

        /** CharSequence */
        val CharSequence = createAny(ClassId.CharSequence)

        /** String : Comparable<String>, CharSequence */
        val String = createComparable(ClassId.String) {
            supertype(ClassId.CharSequence)
        }

        /** Enum<E : Enum<E>> : Comparable<E> */
        val Enum = createAny(ClassId.Enum) {
            val e = TypeParameterId("E")

            val upperbounds = mutableSetOf<Type>()

            parameter(e) {
                variance = KVariance.INVARIANT

                upperbound(ClassId.Enum) {
                    argument {
                        variance = KVariance.INVARIANT
                        type(ClassId.Enum, e)
                    }
                }
            }

            supertype(ClassId.Comparable) {
                argument {
                    variance = KVariance.INVARIANT
                    type(ClassId.Enum, e)
                }
            }
        }

        /** Array<T> */
        val Array = createAny(ClassId.Array) {
            val t = TypeParameterId("T")

            parameter(t) {
                variance = KVariance.INVARIANT
            }
        }

        /** BooleanArray */
        val BooleanArray = createAny(ClassId.BooleanArray)

        /** ByteArray */
        val ByteArray = createAny(ClassId.ByteArray)

        /** ShortArray */
        val ShortArray = createAny(ClassId.ShortArray)

        /** IntArray */
        val IntArray = createAny(ClassId.IntArray)

        /** LongArray */
        val LongArray = createAny(ClassId.LongArray)

        /** FloatArray */
        val FloatArray = createAny(ClassId.FloatArray)

        /** DoubleArray */
        val DoubleArray = createAny(ClassId.DoubleArray)

        /** CharArray */
        val CharArray = createAny(ClassId.CharArray)

        /** Iterator<out T> */
        val Iterator = createAny(ClassId.Iterator) {
            val t = TypeParameterId("T")

            parameter(t) {
                variance = KVariance.OUT
            }
        }

        /** Throwable */
        val Throwable = createAny(ClassId.Throwable)

        /** Comparable<in T> */
        val Comparable = createAny(ClassId.Comparable) {
            val t = TypeParameterId("T")

            parameter(t) {
                variance = KVariance.IN
            }
        }

        /** Function<out R> */
        val Function = createAny(ClassId.Function) {
            val r = TypeParameterId("R")

            parameter(r) {
                variance = KVariance.OUT
            }
        }

        /** Iterable<out T> */
        val Iterable = createAny(ClassId.Iterable) {
            val t = TypeParameterId("T")

            parameter(t) {
                variance = KVariance.OUT
            }
        }

        /** Collection<out E> : Iterable<E> */
        val Collection = createAny(ClassId.Collection) {
            val e = TypeParameterId("E")

            parameter(e) {
                variance = KVariance.OUT
            }

            supertype(ClassId.Iterable) {
                argument {
                    variance = KVariance.INVARIANT
                    type = TypeParameterReference.create(e, ClassId.Collection)
                }
            }
        }

        /** List<out E> : Collection<E> */
        val List = createAny(ClassId.List) {
            val e = TypeParameterId("E")

            parameter(e) {
                variance = KVariance.OUT
            }

            supertype(ClassId.Collection) {
                argument {
                    variance = KVariance.INVARIANT
                    type = TypeParameterReference.create(e, ClassId.List)
                }
            }
        }
    }

    fun isSubclassOf(parent: ClassId): Boolean {
        return id == parent || supertypes.any { it().isSubclassOf(parent) }
    }

    fun createType(nullable: Boolean = false): ClassReference {
        return ClassReference(id, parameters.map { StarProjection }, nullable)
    }

    fun createType(nullable: Boolean, arguments: List<Projection>): ClassReference {
        if (arguments.size != parameters.size) error("invalid argument count")
        return ClassReference(id, arguments, nullable)
    }

    override fun toString(): String = buildString {
        append("class ")
        append(id)

        if (parameters.isNotEmpty()) {
            append("<")
            for ((index, parameter) in parameters.withIndex()) {
                if (index > 0) {
                    append(", ")
                }
                append(parameter)
            }
            append(">")
        }

        if (supertypes.isNotEmpty()) {
            append(" : ")
            for ((index, supertype) in supertypes.withIndex()) {
                if (index > 0) {
                    append(", ")
                }
                append(supertype)
            }
        }
    }
}
