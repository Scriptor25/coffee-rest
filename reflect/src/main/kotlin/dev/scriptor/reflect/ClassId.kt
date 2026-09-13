package dev.scriptor.reflect

@JvmInline
value class ClassId(val value: String) {
    override fun toString(): String = value

    operator fun invoke(): Class = getClass(this)
}
