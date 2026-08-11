package dev.scriptor.reflect

data class TypeParameterReference(
    val parameter: TypeParameter,
    override val nullable: Boolean,
) : Type {
    override fun toString(): String {
        return buildString {
            append(parameter.name)

            if (nullable) {
                append("?")
            }
        }
    }
}
