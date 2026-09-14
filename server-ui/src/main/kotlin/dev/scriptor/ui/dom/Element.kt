package dev.scriptor.ui.dom

open class Element(
    val tag: String,
    val attributes: List<Attribute>,
    val children: List<Node>,
) : Node {

    override fun toXmlString(): String =
        "<$tag${
            attributes.joinToString(
                " ",
                " ",
                transform = Attribute::toXmlString,
            )
        }>${
            children.joinToString(
                "",
                transform = Node::toXmlString,
            )
        }</$tag>"
}
