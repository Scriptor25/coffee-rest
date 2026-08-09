package dev.scriptor.reflect

internal object ClassPath {
    val classes: Map<ClassId, Class> = mapOf(
        // generated
    )
}

fun ClassId.getClass(): Class = ClassPath.classes[this] ?: error("undefined class $this")
