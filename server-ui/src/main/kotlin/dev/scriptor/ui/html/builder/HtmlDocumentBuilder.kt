package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.*
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.JsNode

class HtmlDocumentBuilder : HtmlBuilder<Document> {

    override val children = mutableListOf<Node>()

    context(context: Bundle)
    override fun build(): Document {
        val script = context.script.build()
        val text = Text(script.joinToString(";", transform = JsNode::toJsString))

        val element = HtmlElement(
            false,
            "script",
            listOf(Attribute("type", "module")),
            listOf(text),
        )

        val body = children.filterIsInstance<HtmlElement>().find { it.tag == "body" }
        if (body != null) {
            children -= body
            children += HtmlElement(
                false,
                "body",
                body.attributes,
                body.children + element,
            )
        }

        return Document("html", children)
    }

    context(_: Bundle)
    fun head(block: HtmlHeadElementBuilder.() -> Unit): Element {
        return element(HtmlHeadElementBuilder(), block)
    }

    context(_: Bundle)
    fun body(vararg attributes: Pair<String, String?>, block: HtmlBodyElementBuilder.() -> Unit): Element {
        return element(HtmlBodyElementBuilder(attributes.map { Attribute(it.first, it.second) }), block)
    }
}
