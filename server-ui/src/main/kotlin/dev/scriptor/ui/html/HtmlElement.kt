package dev.scriptor.ui.html

import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Element
import dev.scriptor.ui.dom.Node

open class HtmlElement(
    val void: Boolean,
    tag: String,
    attributes: List<Attribute>,
    children: List<Node>,
) : Element(
    tag,
    attributes,
    children,
) {
    override fun toXmlString(): String = when (void) {
        true -> "<$tag${
            if (attributes.isEmpty()) ""
            else attributes.joinToString(
                " ",
                " ",
                transform = Attribute::toXmlString,
            )
        }>"

        false -> super.toXmlString()
    }
}
