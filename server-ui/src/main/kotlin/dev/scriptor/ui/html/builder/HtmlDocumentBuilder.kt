package dev.scriptor.ui.html.builder

import dev.scriptor.ui.BuilderContext
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.dom.Element
import dev.scriptor.ui.dom.Node

class HtmlDocumentBuilder : HtmlBuilder<Document> {

    override val children = mutableListOf<Node>()

    context(context: BuilderContext)
    override fun build(): Document {
        return Document("html", children)
    }

    context(_: BuilderContext)
    fun head(block: HtmlHeadElementBuilder.() -> Unit): Element {
        return element(HtmlHeadElementBuilder(), block)
    }

    context(_: BuilderContext)
    fun body(vararg attributes: Pair<String, String?>, block: HtmlBodyElementBuilder.() -> Unit): Element {
        return element(HtmlBodyElementBuilder(attributes.map { Attribute(it.first, it.second) }), block)
    }
}
