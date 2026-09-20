package dev.scriptor.ui.dom

open class Document(
    val type: String,
    val children: List<Node>,
) {

    fun toXmlString(): String = """
        <!DOCTYPE $type>
        <$type>${children.joinToString("", transform = Node::toXmlString)}</$type>
    """.trimIndent()
}
