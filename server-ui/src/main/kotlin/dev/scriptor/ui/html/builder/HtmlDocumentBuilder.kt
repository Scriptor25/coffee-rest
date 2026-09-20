package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.css.CssNode
import dev.scriptor.ui.dom.*
import dev.scriptor.ui.dom.builder.WithBundle
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.JsNode

class HtmlDocumentBuilder : HtmlBuilder<Document> {

    override val children = mutableListOf<Node>()

    context(bundle: Bundle)
    override fun build(): Document {
        val script = bundle.script.build()
        val style = bundle.style.build()

        val append = mutableListOf<Node>()

        if (script.isNotEmpty()) {
            val source = Raw(script.joinToString(";", transform = JsNode::toJsString))

            val element = HtmlElement(
                false,
                "script",
                listOf(Attribute("type", AttributeValue.StringValue("module"))),
                listOf(source),
            )

            append += element
        }

        if (style.isNotEmpty()) {
            val source = Raw(style.joinToString("", transform = CssNode::toCssString))

            val element = HtmlElement(
                false,
                "style",
                listOf(),
                listOf(source),
            )

            append += element
        }

        if (append.isNotEmpty()) {
            val body = children.filterIsInstance<HtmlElement>().find { it.tag == "body" }
            if (body != null) {
                children -= body
                children += HtmlElement(
                    false,
                    "body",
                    body.attributes,
                    body.children + append,
                )
            }
        }

        return Document("html", children)
    }

    context(_: Bundle)
    fun head(block: WithBundle<HtmlHeadElementBuilder> = {}): Element {
        return element(HtmlHeadElementBuilder(), block)
    }

    context(_: Bundle)
    fun body(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlBodyElementBuilder> = {},
    ): Element {
        val attributes = HtmlAttributeBuilder().apply(attributeBlock).build()
        return element(HtmlBodyElementBuilder(attributes), block)
    }
}
