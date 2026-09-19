package dev.scriptor.server.jvm.annotation

import dev.scriptor.server.Signal
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Handle(vararg val value: KClass<out Signal>)
