package dev.scriptor.server.jvm.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
