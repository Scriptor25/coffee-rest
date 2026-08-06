package dev.scriptor.server.reflect

data class BaseClass(
    val parameters: List<TypeParameter>,
    val supertypes: List<Type>,
) : Classifier {

    fun createType(arguments: List<Projection>, nullable: Boolean): Type {
        if (arguments.size != parameters.size) error("invalid arguments, ${arguments.size} != ${parameters.size}")

        // TODO: check bounds of parameters

        return Type(this, arguments, nullable)
    }

    fun isSubclassOf(parent: BaseClass): Boolean {
        return supertypes
            .mapNotNull { type -> type.classifier as? BaseClass }
            .any { base -> base == parent || base.isSubclassOf(parent) }
    }
}
