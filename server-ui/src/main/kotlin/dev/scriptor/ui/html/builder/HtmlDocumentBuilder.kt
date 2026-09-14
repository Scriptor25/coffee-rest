package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.dom.Node
import dev.scriptor.ui.html.HtmlBodyElement
import dev.scriptor.ui.html.HtmlHeadElement

class HtmlDocumentBuilder : HtmlBuilder<Document> {

    override val children = mutableListOf<Node>()

    override fun build(): Document {
        return Document("html", children)
    }

    fun head(block: HtmlHeadElementBuilder.() -> Unit): HtmlHeadElement {
        return element(HtmlHeadElementBuilder(), block)
    }

    fun body(block: HtmlBodyElementBuilder.() -> Unit): HtmlBodyElement {
        return element(HtmlBodyElementBuilder(), block)
    }
}
