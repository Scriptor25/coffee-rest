package dev.scriptor.reflect

data class ClassReference(
    val id: ClassId,
    val arguments: List<Projection>,
    override val nullable: Boolean,
) : Type {
    class Builder(val id: ClassId) {
        val arguments = mutableListOf<Projection>()
        var nullable: Boolean = false

        fun argument(block: TypeProjection.Builder.() -> Unit = {}) {
            arguments += TypeProjection.Builder().apply(block).build()
        }

        fun star() {
            arguments += StarProjection
        }

        fun build(): ClassReference = ClassReference(id, arguments, nullable)
    }

    companion object {
        fun create(id: ClassId, block: Builder.() -> Unit = {}): ClassReference {
            return Builder(id).apply(block).build()
        }
    }

    operator fun invoke(): Class {
        return id()
    }

    override fun toString(): String = buildString {
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
