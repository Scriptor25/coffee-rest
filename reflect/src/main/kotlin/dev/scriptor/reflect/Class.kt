package dev.scriptor.reflect

data class Class(
    val id: ClassId,
    val parameters: List<TypeParameter>,
    val supertypes: Set<ClassReference>,
) {
    class Builder(val id: ClassId) {
        val parameters = mutableListOf<TypeParameter>()
        val supertypes = mutableSetOf<ClassReference>()

        fun parameter(id: TypeParameterId, block: TypeParameter.Builder.() -> Unit = {}): TypeParameter {
            val parameter = TypeParameter.Builder(id).apply(block).build()
            parameters += parameter
            return parameter
        }

        fun supertype(id: ClassId, block: ClassReference.Builder.() -> Unit = {}): ClassReference {
            val supertype = ClassReference.Builder(id).apply(block).build()
            supertypes += supertype
            return supertype
        }

        fun build(): Class = Class(id, parameters, supertypes)
    }

    fun isSubclassOf(parent: ClassId): Boolean {
        return id == parent || supertypes.any { it().isSubclassOf(parent) }
    }

    fun createType(nullable: Boolean = false): ClassReference {
        return ClassReference(id, parameters.map { StarProjection }, nullable)
    }

    fun createType(nullable: Boolean, arguments: List<Projection>): ClassReference {
        if (arguments.size != parameters.size) error("invalid argument count")
        return ClassReference(id, arguments, nullable)
    }

    override fun toString(): String = buildString {
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
