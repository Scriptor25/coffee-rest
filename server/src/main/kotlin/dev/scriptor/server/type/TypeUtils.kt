package dev.scriptor.server.type

import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf

fun isAssignable(dst: KType, src: KType): Boolean {

    if (dst == src) {
        return true
    }

    if (!dst.isMarkedNullable && src.isMarkedNullable) {
        return false
    }

    val dstClassifier = dst.classifier

    if (dstClassifier is KTypeParameter) {
        return satisfiesBounds(src, dstClassifier)
    }

    val dstClass = dstClassifier as? KClass<*> ?: return false

    val resolved = resolveSupertype(src, dstClass) ?: return false

    val srcClassifier = resolved.classifier

    if (srcClassifier !is KClass<*>) {
        return false
    }

    if (!srcClassifier.isSubclassOf(dstClass)) {
        return false
    }

    val dstArguments = dst.arguments
    val srcArguments = resolved.arguments

    if (dstArguments.size != srcArguments.size) {
        return false
    }

    for ((dstArgument, srcArgument) in dstArguments.zip(srcArguments)) {

        if (dstArgument.type == null) {
            continue
        }

        if (srcArgument.type == null) {
            return false
        }

        val dstType = dstArgument.type!!
        val srcType = srcArgument.type!!

        when (dstArgument.variance ?: KVariance.INVARIANT) {

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

    return true
}

private fun satisfiesBounds(type: KType, parameter: KTypeParameter): Boolean =
    parameter.upperBounds.all { isAssignable(it, type) }

private fun resolveSupertype(type: KType, target: KClass<*>): KType? {
    val classifier = type.classifier as? KClass<*> ?: return null

    if (classifier == target) {
        return type
    }

    val mapping = classifier
        .typeParameters
        .zip(type.arguments)
        .associate { (parameter, argument) -> parameter to argument.type }

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

private fun substitute(type: KType, mapping: Map<KTypeParameter, KType?>): KType {
    val classifier = type.classifier

    if (classifier is KTypeParameter) {
        return mapping[classifier] ?: type
    }

    val klass = classifier as? KClass<*> ?: return type

    if (type.arguments.isEmpty()) {
        return type
    }

    return klass.createType(
        arguments = type.arguments.map { argument ->

            val argumentType = argument.type

            if (argumentType == null)
                KTypeProjection.STAR
            else
                KTypeProjection(
                    argument.variance,
                    substitute(argumentType, mapping),
                )
        },
        nullable = type.isMarkedNullable,
    )
}
