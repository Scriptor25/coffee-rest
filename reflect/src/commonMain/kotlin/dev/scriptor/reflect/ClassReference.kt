package dev.scriptor.reflect

data class ClassReference(
    val id: ClassId,
    val arguments: List<Projection>,
    override val nullable: Boolean,
) : Type {
    override fun toString(): String {
        return buildString {
            append(id)

            if (arguments.isNotEmpty()) {
                append("<")
                for ((index, argument) in arguments.withIndex()) {
                    if (index > 0) {
                        append(", ")
                    }
                    append(argument)
                }
                append(">")
            }

            if (nullable) {
                append("?")
            }
        }
    }
}
