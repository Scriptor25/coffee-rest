package dev.scriptor.reflect

import kotlin.reflect.KClass

internal object ClassPath {
    val classes: Map<ClassId, Class> = mapOf(
        // generated
    )
}

fun getClass(id: ClassId): Class = ClassPath.classes[id] ?: error("undefined class $id")

fun getClass(classifier: KClass<*>): Class {
    val name = classifier.qualifiedName
        ?: error("class $classifier does not have a qualified name")

    return getClass(ClassId(name))
}
