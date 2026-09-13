package dev.scriptor.reflect

import java.lang.Class.forName
import kotlin.reflect.KClass

private val map = mutableMapOf<ClassId, Class>()

fun getClass(id: ClassId): Class {
    return map.computeIfAbsent(id) {
        val klass = forName(id.value, false, Thread.currentThread().contextClassLoader).kotlin

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
    return getClass(getClassId(classifier))
}

inline fun <reified T> getClass(): Class {
    return getClass(T::class)
}
