package dev.scriptor.server.jvm.annotation

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireAuth
