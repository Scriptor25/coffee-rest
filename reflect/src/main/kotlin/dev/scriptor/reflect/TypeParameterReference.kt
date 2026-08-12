package dev.scriptor.reflect

data class TypeParameterReference(
    val id: TypeParameterId,
    val cls: ClassId,
    override val nullable: Boolean,
) : Type {
    class Builder(val cls: ClassId, val id: TypeParameterId) {
        var nullable: Boolean = false

        fun build(): TypeParameterReference = TypeParameterReference(id, cls, nullable)
    }

    companion object {
        fun create(id: TypeParameterId, cls: ClassId, block: Builder.() -> Unit = {}): TypeParameterReference {
            return Builder(cls, id).apply(block).build()
        }
    }

    operator fun invoke(): TypeParameter {
        return context(cls()) { id() }
    }

    override fun toString(): String = buildString {
        append(id)

        if (nullable) {
            append("?")
        }
    }
}
