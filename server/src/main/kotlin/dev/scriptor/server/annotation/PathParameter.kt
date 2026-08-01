package dev.scriptor.server.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PathParameter(val value: String = "") // TODO: replace with context(...)
