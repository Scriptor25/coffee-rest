package dev.scriptor.server.annotation

import dev.scriptor.server.http.Method

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Route(
    val path: String,
    val method: Method = Method.GET,
    val accept: String = "",
    val result: String = "",
)
