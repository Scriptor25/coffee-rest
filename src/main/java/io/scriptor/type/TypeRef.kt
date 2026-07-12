package io.scriptor.type

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class TypeRef<T> protected constructor() {

    val type: Type

    init {
        val superclass = this.javaClass.genericSuperclass
        if (superclass is ParameterizedType) {
            this.type = superclass.actualTypeArguments[0]
        } else {
            throw IllegalStateException()
        }
    }
}
