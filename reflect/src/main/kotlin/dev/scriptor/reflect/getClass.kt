package dev.scriptor.reflect

import kotlin.reflect.KClass

private val map = mutableMapOf(
    ClassId.Any to Class.Any,
    ClassId.Nothing to Class.Nothing,
    ClassId.Unit to Class.Unit,
    ClassId.Boolean to Class.Boolean,
    ClassId.Number to Class.Number,
    ClassId.Byte to Class.Byte,
    ClassId.Short to Class.Short,
    ClassId.Int to Class.Int,
    ClassId.Long to Class.Long,
    ClassId.Float to Class.Float,
    ClassId.Double to Class.Double,
    ClassId.Char to Class.Char,
    ClassId.CharSequence to Class.CharSequence,
    ClassId.String to Class.String,
    ClassId.Enum to Class.Enum,
    ClassId.Array to Class.Array,
    ClassId.BooleanArray to Class.BooleanArray,
    ClassId.ByteArray to Class.ByteArray,
    ClassId.ShortArray to Class.ShortArray,
    ClassId.IntArray to Class.IntArray,
    ClassId.LongArray to Class.LongArray,
    ClassId.FloatArray to Class.FloatArray,
    ClassId.DoubleArray to Class.DoubleArray,
    ClassId.CharArray to Class.CharArray,
    ClassId.Iterator to Class.Iterator,
    ClassId.Throwable to Class.Throwable,
    ClassId.Comparable to Class.Comparable,
    ClassId.Function to Class.Function,
)

fun getClass(id: ClassId): Class {
    return map.computeIfAbsent(id) {
        val klass = Thread.currentThread().contextClassLoader.loadClass(id.value).kotlin

        val parameters = klass.typeParameters.map { parameter ->
            val upperbounds = parameter.upperBounds
                .map(::getType)
                .toSet()

            TypeParameter(
                TypeParameterId(parameter.name),
                parameter.variance,
                upperbounds,
            )
        }

        val supertypes = klass.supertypes
            .map(::getType)
            .filterIsInstance<ClassReference>()
            .toSet()

        Class(
            id,
            parameters,
            supertypes,
        )
    }
}

fun getClass(classifier: KClass<*>): Class {
    val name = classifier.qualifiedName
        ?: error("class $classifier does not have a qualified name")

    return getClass(ClassId(name))
}
