package dev.scriptor.reflect

import kotlin.reflect.KClass

fun getClassId(classifier: KClass<*>): ClassId {
    return ClassId(classifier.javaObjectType.name)
}

inline fun <reified T> getClassId(): ClassId {
    return getClassId(T::class)
}
