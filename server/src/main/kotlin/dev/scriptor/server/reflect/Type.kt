package dev.scriptor.server.reflect

data class Type(
    val classifier: Classifier,
    val arguments: List<Projection>,
    val nullable: Boolean,
)
