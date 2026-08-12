package dev.scriptor.reflect

@JvmInline
value class TypeParameterId(val value: String) {
    override fun toString(): String = value

    context(c: Class)
    operator fun invoke(): TypeParameter = c.parameters.first { it.id == this }
}
