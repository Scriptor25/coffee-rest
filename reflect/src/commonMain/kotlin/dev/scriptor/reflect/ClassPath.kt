package dev.scriptor.reflect

import kotlin.reflect.KClass

fun getClass(id: ClassId): Class = TODO("generate class metadata at compile time")[id] ?: error("undefined class $id")

fun getClass(classifier: KClass<*>): Class {
    val name = classifier.qualifiedName
        ?: error("class $classifier does not have a qualified name")

    return getClass(ClassId(name))
}
