package dev.scriptor.net

import kotlin.reflect.KClass

interface SocketOption<T : Any> {
    val name: String
    val type: KClass<T>
}
