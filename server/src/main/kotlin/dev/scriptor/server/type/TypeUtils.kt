package dev.scriptor.server.type

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KVariance
import kotlin.reflect.full.isSubclassOf

fun isAssignable(dst: KType, src: KType): Boolean {

    if (dst == src) {
        return true
    }

    if (!dst.isMarkedNullable && src.isMarkedNullable) {
        return false
    }

    val dstClassifier = dst.classifier
    val srcClassifier = src.classifier

    when {
        dstClassifier is KClass<*> && srcClassifier is KClass<*> ->
            if (!srcClassifier.isSubclassOf(dstClassifier))
                return false

        dstClassifier is KTypeParameter ->
            return satisfiesBounds(src, dstClassifier)

        srcClassifier is KTypeParameter ->
            return satisfiesBounds(dst, srcClassifier)

        else ->
            return false
    }

    val dstArguments = dst.arguments
    val srcArguments = src.arguments

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

            KVariance.INVARIANT ->
                if (dstType != srcType)
                    return false

            KVariance.OUT ->
                if (!isAssignable(dstType, srcType))
                    return false

            KVariance.IN ->
                if (!isAssignable(srcType, dstType))
                    return false
        }
    }

    return true
}

private fun satisfiesBounds(type: KType, parameter: KTypeParameter): Boolean =
    parameter.upperBounds.all { isAssignable(it, type) }
