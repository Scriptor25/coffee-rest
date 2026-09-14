package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Node
import dev.scriptor.ui.html.HtmlElement

open class HtmlElementBuilder<E : HtmlElement>(
    val build: (List<Attribute>, List<Node>) -> E,
) : HtmlBuilder<E> {

    override val children = mutableListOf<Node>()

    override fun build(): E {
        return build(listOf(), children)
    }
}
