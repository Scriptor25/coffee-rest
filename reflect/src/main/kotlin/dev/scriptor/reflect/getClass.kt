package dev.scriptor.reflect

import kotlin.reflect.KClass

private val mapIdToClass = mutableMapOf(
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

    ClassId.Iterable to Class.Iterable,
    ClassId.Collection to Class.Collection,
    ClassId.List to Class.List,
)

fun getClass(id: ClassId): Class {
    return mapIdToClass.computeIfAbsent(id) {
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

private val mapKToId = mutableMapOf(
    Any::class to ClassId.Any,
    Nothing::class to ClassId.Nothing,
    Unit::class to ClassId.Unit,
    Boolean::class to ClassId.Boolean,
    Number::class to ClassId.Number,
    Byte::class to ClassId.Byte,
    Short::class to ClassId.Short,
    Int::class to ClassId.Int,
    Long::class to ClassId.Long,
    Float::class to ClassId.Float,
    Double::class to ClassId.Double,
    Char::class to ClassId.Char,
    CharSequence::class to ClassId.CharSequence,
    String::class to ClassId.String,
    Enum::class to ClassId.Enum,
    Array::class to ClassId.Array,
    BooleanArray::class to ClassId.BooleanArray,
    ByteArray::class to ClassId.ByteArray,
    ShortArray::class to ClassId.ShortArray,
    IntArray::class to ClassId.IntArray,
    LongArray::class to ClassId.LongArray,
    FloatArray::class to ClassId.FloatArray,
    DoubleArray::class to ClassId.DoubleArray,
    CharArray::class to ClassId.CharArray,
    Iterator::class to ClassId.Iterator,
    Throwable::class to ClassId.Throwable,
    Comparable::class to ClassId.Comparable,
    Function::class to ClassId.Function,

    Iterable::class to ClassId.Iterable,
    Collection::class to ClassId.Collection,
    List::class to ClassId.List,
)

fun getClass(classifier: KClass<*>): Class {
    val id = mapKToId.computeIfAbsent(classifier) {
        val name = classifier.javaObjectType.canonicalName
            ?: error("class $classifier does not have a qualified name")

        ClassId(name)
    }

    return getClass(id)
}
