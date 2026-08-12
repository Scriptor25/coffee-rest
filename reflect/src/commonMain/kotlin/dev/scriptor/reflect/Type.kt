package dev.scriptor.reflect

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.typeOf

sealed interface Type {
    val nullable: Boolean
}

fun getType(type: KType): Type {
    val nullable = type.isMarkedNullable

    return when (val classifier = type.classifier) {
        is KTypeParameter -> {
            TypeParameterReference(
                TypeParameter(
                    classifier.name,
                    classifier.variance,
                    classifier.upperBounds.map(::getType),
                ),
                nullable,
            )
        }

        is KClass<*> -> {
            val name = classifier.qualifiedName
                ?: error("class $classifier does not have a qualified name")

            ClassReference(
                ClassId(name),
                type.arguments.map {
                    if (it.type == null || it.variance == null)
                        StarProjection
                    else TypeProjection(
                        getType(it.type!!),
                        it.variance!!,
                    )
                },
                nullable,
            )
        }

        else -> error("unsupported classifier $classifier")
    }
}

inline fun <reified T> getType(): Type {
    return getType(typeOf<T>())
}
