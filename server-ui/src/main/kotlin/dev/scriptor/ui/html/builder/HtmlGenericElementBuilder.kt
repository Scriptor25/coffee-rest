package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.css.CssNode
import dev.scriptor.ui.css.builder.CssNodesBuilder
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.JsNode
import dev.scriptor.ui.js.builder.JsNodesBuilder

open class HtmlGenericElementBuilder(
    void: Boolean,
    tag: String,
    attributes: List<Attribute>,
) : HtmlElementBuilder(void, tag, attributes) {

    context(_: Bundle)
    fun htmlElement(
        void: Boolean,
        tag: String,
        vararg attributes: Pair<String, String?>,
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(void, tag, attributes.map { Attribute(it.first, it.second) }, block)
    }

    context(_: Bundle)
    fun htmlElement(
        void: Boolean,
        tag: String,
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlGlobalAttributeBuilder()
        builder.apply(attributeBlock)
        val attributes = builder.build()

        return htmlElement(void, tag, attributes, block)
    }

    context(_: Bundle)
    fun htmlElement(
        void: Boolean,
        tag: String,
        attributes: List<Attribute>,
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlGenericElementBuilder(void, tag, attributes)
        builder.apply(block)
        val element = builder.build()
        children += element
        return element
    }

    context(_: Bundle)
    fun main(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "main", attributeBlock, block)
    }

    context(_: Bundle)
    fun div(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "div", attributeBlock, block)
    }

    context(_: Bundle)
    fun span(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "span", attributeBlock, block)
    }

    context(_: Bundle)
    fun p(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "p", attributeBlock, block)
    }

    context(_: Bundle)
    fun h1(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h1", attributeBlock, block)
    }

    context(_: Bundle)
    fun h2(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h2", attributeBlock, block)
    }

    context(_: Bundle)
    fun h3(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h3", attributeBlock, block)
    }

    context(_: Bundle)
    fun h4(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h4", attributeBlock, block)
    }

    context(_: Bundle)
    fun h5(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h5", attributeBlock, block)
    }

    context(_: Bundle)
    fun h6(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h6", attributeBlock, block)
    }

    context(_: Bundle)
    fun button(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "button", attributeBlock, block)
    }

    context(_: Bundle)
    fun section(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "section", attributeBlock, block)
    }

    context(_: Bundle)
    fun script(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: JsNodesBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = JsNodesBuilder()
        builder.apply(block)
        val script = builder.build()
        val text = script.joinToString(";", transform = JsNode::toJsString)

        return htmlElement(
            false,
            "script",
            {
                attributeBlock()
                this["type"] = "module"
            },
        ) { +text }
    }

    context(_: Bundle)
    fun style(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: CssNodesBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = CssNodesBuilder()
        builder.apply(block)
        val style = builder.build()
        val text = style.joinToString(" ", transform = CssNode::toCssString)

        return htmlElement(
            false,
            "style",
            attributeBlock,
        ) { +text }
    }
}
