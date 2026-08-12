package dev.scriptor.reflect

data class Class(
    val id: ClassId,
    val parameters: List<TypeParameter>,
    val supertypes: List<ClassReference>,
) {
    fun isSubclassOf(parent: ClassId): Boolean {
        return id == parent || supertypes.any { it.id().isSubclassOf(parent) }
    }

    val starProjectedType: ClassReference
        get() = ClassReference(id, parameters.map { StarProjection }, false)

    override fun toString(): String {
        return buildString {
            append("class ")
            append(id)

            if (parameters.isNotEmpty()) {
                append("<")
                for ((index, parameter) in parameters.withIndex()) {
                    if (index > 0) {
                        append(", ")
                    }
                    append(parameter)
                }
                append(">")
            }

            if (supertypes.isNotEmpty()) {
                append(" : ")
                for ((index, supertype) in supertypes.withIndex()) {
                    if (index > 0) {
                        append(", ")
                    }
                    append(supertype)
                }
            }
        }
    }
}
