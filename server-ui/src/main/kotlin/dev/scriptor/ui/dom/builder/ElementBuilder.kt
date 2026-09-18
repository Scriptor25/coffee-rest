package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Element
import dev.scriptor.ui.dom.Node

open class ElementBuilder(
    val tag: String,
    val attributes: List<Attribute>,
) : Builder<Element> {

    override val children = mutableListOf<Node>()

    context(bundle: Bundle)
    override fun build(): Element {
        return Element(
            tag,
            attributes,
            children,
        )
    }
}
