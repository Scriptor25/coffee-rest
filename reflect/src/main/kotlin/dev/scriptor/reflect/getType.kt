package dev.scriptor.reflect

import kotlin.reflect.*

fun getType(type: KType): Type {
    val nullable = type.isMarkedNullable

    return when (val classifier = type.classifier) {
        is KClass<*> -> getType(classifier, type.arguments, nullable)

        else -> error("unsupported classifier $classifier")
    }
}

fun getType(id: ClassId, type: KType): Type {
    val nullable = type.isMarkedNullable

    return when (val classifier = type.classifier) {
        is KTypeParameter -> getType(id, classifier, nullable)
        is KClass<*> -> getType(classifier, type.arguments, nullable)

        else -> error("unsupported classifier $classifier")
    }
}

private fun getType(cls: ClassId, type: KTypeParameter, nullable: Boolean): TypeParameterReference {
    val id = TypeParameterId(type.name)

    return TypeParameterReference(id, cls, nullable)
}

private fun getType(type: KClass<*>, arguments: List<KTypeProjection>, nullable: Boolean): ClassReference {
    val name = type.qualifiedName
        ?: error("$type does not have a qualified name")

    val id = ClassId(name)

    return ClassReference(
        id,
        arguments.map {
            if (it.type == null || it.variance == null)
                StarProjection
            else TypeProjection(
                it.variance!!,
                getType(id, it.type!!),
            )
        },
        nullable,
    )
}

inline fun <reified T> getType(): Type = getType(typeOf<T>())
