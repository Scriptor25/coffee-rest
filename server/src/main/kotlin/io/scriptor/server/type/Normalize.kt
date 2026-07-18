package io.scriptor.server.type

import java.lang.reflect.Type

fun Class<*>.normalize(): Class<*> = kotlin.javaPrimitiveType ?: kotlin.javaObjectType

fun Type.normalize(): Type = if (this is Class<*>) normalize() else this
