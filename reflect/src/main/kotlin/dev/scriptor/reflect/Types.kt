package dev.scriptor.reflect

import kotlin.reflect.KVariance

private val assignableMap = mutableMapOf<Pair<Type, Type>, Boolean>()

fun isAssignable(dst: Type, src: Type): Boolean {
    if (dst to src in assignableMap) {
        return assignableMap[dst to src]!!
    }

    val result = dst == src || (dst.nullable || !src.nullable) && when (dst) {
        is ClassReference -> isAssignable(dst, src)
        is TypeParameterReference -> satisfiesBounds(src, dst)
    }

    assignableMap[dst to src] = result
    return result
}

private fun isAssignable(dst: ClassReference, src: Type): Boolean {
    val src = resolveSupertype(src, dst.id)
        ?: return false

    val srcCls = src()

    if (!srcCls.isSubclassOf(dst.id)) {
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

private fun satisfiesBounds(type: Type, parameter: TypeParameterReference): Boolean {
    return parameter().upperbounds.all { isAssignable(it, type) }
}

private val supertypeMap = mutableMapOf<Pair<Type, ClassId>, ClassReference?>()

private fun resolveSupertype(type: Type, target: ClassId): ClassReference? {
    if (type to target in supertypeMap) {
        return supertypeMap[type to target]
    }

    val result = when (type) {
        is ClassReference ->
            if (type.id == target) type
            else {
                val cls = type.id()

                val mapping = cls.parameters
                    .zip(type.arguments)
                    .associate { (parameter, argument) -> parameter.id to argument }

                var result: ClassReference? = null

                for (supertype in cls.supertypes) {
                    val substituted = substitute(
                        supertype,
                        mapping,
                    )

                    result = resolveSupertype(
                        substituted,
                        target,
                    ) ?: continue

                    break
                }

                result
            }

        else -> null
    }

    supertypeMap[type to target] = result
    return result
}

private fun substitute(type: Type, mapping: Map<TypeParameterId, Projection>): Type {
    return when (type) {
        is ClassReference -> {
            if (type.arguments.isEmpty()) type
            else ClassReference(
                type.id,
                type.arguments.map { argument ->
                    argument as? StarProjection
                        ?: TypeProjection(
                            (argument as TypeProjection).variance,
                            substitute(argument.type, mapping),
                        )
                },
                type.nullable,
            )
        }

        is TypeParameterReference -> {
            if (type.id !in mapping) type
            else when (val mapped = mapping[type.id]!!) {
                is StarProjection -> type().upperbounds.firstOrNull()
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
