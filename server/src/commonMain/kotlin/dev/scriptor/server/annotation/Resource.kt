package dev.scriptor.server.annotation

import dev.scriptor.server.http.Method

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Resource(
    val path: String,
    val method: Method = Method.GET,
    val accept: String = "*/*",
    val result: String = "*/*",
)
