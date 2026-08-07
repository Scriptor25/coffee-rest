package dev.scriptor.server.reflect

interface Parameter {
    val type: Type
    val annotations: List<Annotation>
}

inline fun <reified T> Parameter.hasAnnotation(): Boolean {}
inline fun <reified T> Parameter.getAnnotation(): T {}
inline fun <reified T> Parameter.findAnnotation(): T? {}
