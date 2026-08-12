package dev.scriptor.reflect

import kotlin.jvm.JvmInline

@JvmInline
value class ClassId(val value: String) {
    override fun toString(): String {
        return value
    }

    companion object {
        val Any = ClassId("kotlin.Any")
    }

    operator fun invoke(): Class = getClass(this)
}
