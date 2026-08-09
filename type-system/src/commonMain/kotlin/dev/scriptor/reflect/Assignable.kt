package dev.scriptor.reflect

import kotlin.reflect.KVariance

fun isAssignable(dst: Type, src: Type): Boolean {

    if (dst == src) {
        return true
    }

    if (!dst.nullable && src.nullable) {
        return false
    }

    return when (dst) {
        is TypeParameterReference -> satisfiesBounds(src, dst.parameter)
        is ClassReference -> isAssignable(dst, src)
    }
}

private fun isAssignable(dst: ClassReference, src: Type): Boolean {
    val src = resolveSupertype(src, dst.id)
        ?: return false

    val srcClass = src.id.getClass()

    if (!srcClass.isSubclassOf(dst.id)) {
        return false
    }

    val dstArguments = dst.arguments
    val srcArguments = src.arguments

    if (dstArguments.size != srcArguments.size) {
        return false
    }

    for ((dstArgument, srcArgument) in dstArguments.zip(srcArguments)) {
        when (dstArgument) {
            // anything can assign to '*'
            is StarProjection -> continue

            is TypeProjection -> when (srcArgument) {
                // '*' cannot assign to anything
                is StarProjection -> return false

                is TypeProjection -> {
                    val dstType = dstArgument.type
                    val srcType = srcArgument.type

                    when (dstArgument.variance) {
                        KVariance.INVARIANT -> {
                            if (dstType != srcType) {
                                return false
                            }
                        }

                        KVariance.OUT -> {
                            if (!isAssignable(dstType, srcType)) {
                                return false
                            }
                        }

                        KVariance.IN -> {
                            if (!isAssignable(srcType, dstType)) {
                                return false
                            }
                        }
                    }
                }
            }
        }
    }

    return true
}

private fun satisfiesBounds(type: Type, parameter: TypeParameter): Boolean {
    return parameter.upperbounds.all { isAssignable(it, type) }
}

private fun resolveSupertype(type: Type, target: ClassId): ClassReference? {
    if (type !is ClassReference) {
        return null
    }

    if (type.id == target) {
        return type
    }

    val clazz = type.id.getClass()

    val mapping = clazz.parameters
        .zip(type.arguments)
        .associate { (parameter, argument) -> parameter to argument }

    for (supertype in clazz.supertypes) {
        val substituted = substitute(
            supertype,
            mapping,
        )

        val result = resolveSupertype(
            substituted,
            target,
        ) ?: continue

        return result
    }

    return null
}

private fun substitute(type: Type, mapping: Map<TypeParameter, Projection>): Type {
    return when (type) {
        is ClassReference -> {
            if (type.arguments.isEmpty()) type
            else ClassReference(
                type.id,
                type.arguments.map { argument ->
                    argument as? StarProjection
                        ?: TypeProjection(
                            substitute((argument as TypeProjection).type, mapping),
                            argument.variance,
                        )
                },
                type.nullable,
            )
        }

        is TypeParameterReference -> {
            if (type.parameter !in mapping) type
            else when (val mapped = mapping[type.parameter]!!) {
                is StarProjection -> type.parameter.upperbounds.firstOrNull()
                    ?: ClassReference(
                        ClassId.Any,
                        emptyList(),
                        type.nullable,
                    )

                is TypeProjection -> mapped.type
            }
        }
    }
}
