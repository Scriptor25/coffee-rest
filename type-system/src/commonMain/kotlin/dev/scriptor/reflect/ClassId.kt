package dev.scriptor.reflect

value class ClassId(val value: String) {
    override fun toString(): String {
        return value
    }

    companion object {
        val Any = ClassId("kotlin.Any")
    }
}
