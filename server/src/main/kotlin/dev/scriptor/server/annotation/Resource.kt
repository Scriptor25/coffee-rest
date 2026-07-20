package dev.scriptor.server.annotation

import dev.scriptor.server.http.HTTPMethod

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Resource(
    val path: String,
    val method: HTTPMethod = HTTPMethod.GET,
    val accept: String = "*/*",
    val result: String = "*/*"
)
