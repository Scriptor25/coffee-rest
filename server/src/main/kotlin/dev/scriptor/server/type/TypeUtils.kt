package dev.scriptor.server.type

import dev.scriptor.server.reflect.*

fun isAssignable(dst: Type, src: Type): Boolean {

    if (dst == src) {
        return true
    }

    if (!dst.nullable && src.nullable) {
        return false
    }

    val dstClassifier = dst.classifier
    if (dstClassifier is TypeParameter) {
        return satisfiesBounds(src, dstClassifier)
    }

    if (dstClassifier !is BaseClass) {
        return false
    }

    val resolved = resolveSupertype(src, dstClassifier)
        ?: return false

    val srcClassifier = resolved.classifier
    if (srcClassifier !is BaseClass) {
        return false
    }

    if (!srcClassifier.isSubclassOf(dstClassifier)) {
        return false
    }

    val dstArguments = dst.arguments
    val srcArguments = resolved.arguments

    if (dstArguments.size != srcArguments.size) {
        return false
    }

    for ((dstArgument, srcArgument) in dstArguments.zip(srcArguments)) {

        if (dstArgument is StarProjection) {
            // convert to anything -> ok
            continue
        }

        if (srcArgument is StarProjection) {
            // convert from anything -> not ok
            return false
        }

        if (dstArgument !is TypeProjection || srcArgument !is TypeProjection) {
            // this point shall never be reached
            return false
        }

        val dstType = dstArgument.type
        val srcType = srcArgument.type

        // TODO: check if this is right

        when (dstArgument.variance) {

            Variance.INVARIANT -> {
                if (dstType != srcType) {
                    return false
                }
            }

            Variance.OUT -> {
                if (!isAssignable(dstType, srcType)) {
                    return false
                }
            }

            Variance.IN -> {
                if (!isAssignable(srcType, dstType)) {
                    return false
                }
            }
        }
    }

    return true
}

private fun satisfiesBounds(type: Type, parameter: TypeParameter): Boolean =
    parameter.upperBounds.all { isAssignable(it, type) }

private fun resolveSupertype(type: Type, target: BaseClass): Type? {
    val classifier = type.classifier as? BaseClass ?: return null

    if (classifier == target) {
        return type
    }

    val mapping = classifier
        .parameters
        .zip(type.arguments)
        .associate { (parameter, argument) ->
            if (argument is TypeProjection)
                parameter to argument.type
            else parameter to null
        }

    for (supertype in classifier.supertypes) {
        val substituted = substitute(
            supertype,
            mapping,
        )

        val result = resolveSupertype(
            substituted,
            target,
        )

        if (result != null) {
            return result
        }
    }

    return null
}

private fun substitute(type: Type, mapping: Map<TypeParameter, Type?>): Type {
    return when (val classifier = type.classifier) {
        is TypeParameter -> mapping[classifier] ?: type

        is BaseClass ->
            if (type.arguments.isEmpty()) type
            else classifier.createType(
                type.arguments.map { argument ->
                    when (argument) {
                        is StarProjection -> StarProjection()
                        is TypeProjection -> TypeProjection(
                            argument.variance,
                            substitute(argument.type, mapping),
                        )

                        else -> error("unsupported projection $argument")
                    }
                },
                type.nullable,
            )

        else -> error("unsupported classifier $classifier")
    }
}
