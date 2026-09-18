package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.css.CssNode
import dev.scriptor.ui.css.builder.CssNodesBuilder
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.JsNode
import dev.scriptor.ui.js.builder.JsNodesBuilder

open class HtmlGenericBuilder(
    void: Boolean,
    tag: String,
    attributes: List<Attribute>,
) : HtmlElementBuilder(void, tag, attributes) {

    context(_: Bundle)
    protected fun htmlElement(
        void: Boolean,
        tag: String,
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlAttributeBuilder()
        builder.apply(attributeBlock)
        val attributes = builder.build()
        return htmlElement(void, tag, attributes, block)
    }

    context(_: Bundle)
    protected fun htmlElement(
        void: Boolean,
        tag: String,
        attributes: List<Attribute>,
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlGenericBuilder(void, tag, attributes)
        builder.apply(block)
        val element = builder.build()
        children += element
        return element
    }

    context(_: Bundle)
    fun script(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
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
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
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

    context(_: Bundle)
    fun main(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "main", attributeBlock, block)
    }

    context(_: Bundle)
    fun div(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "div", attributeBlock, block)
    }

    context(_: Bundle)
    fun span(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "span", attributeBlock, block)
    }

    context(_: Bundle)
    fun p(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "p", attributeBlock, block)
    }

    context(_: Bundle)
    fun h1(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h1", attributeBlock, block)
    }

    context(_: Bundle)
    fun h2(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h2", attributeBlock, block)
    }

    context(_: Bundle)
    fun h3(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h3", attributeBlock, block)
    }

    context(_: Bundle)
    fun h4(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h4", attributeBlock, block)
    }

    context(_: Bundle)
    fun h5(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h5", attributeBlock, block)
    }

    context(_: Bundle)
    fun h6(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h6", attributeBlock, block)
    }

    context(_: Bundle)
    fun button(
        attributeBlock: HtmlButtonElementAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlButtonElementAttributeBuilder()
        builder.attributeBlock()
        val attributes = builder.build()
        return htmlElement(false, "button", attributes, block)
    }

    context(_: Bundle)
    fun section(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "section", attributeBlock, block)
    }

    context(_: Bundle)
    fun blockquote(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "blockquote", attributeBlock, block)
    }

    context(_: Bundle)
    fun cite(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "cite", attributeBlock, block)
    }

    context(_: Bundle)
    fun ul(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "ul", attributeBlock, block)
    }

    context(_: Bundle)
    fun ol(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "ol", attributeBlock, block)
    }

    context(_: Bundle)
    fun li(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "li", attributeBlock, block)
    }

    context(_: Bundle)
    fun a(
        attributeBlock: HtmlAnchorElementAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlAnchorElementAttributeBuilder()
        builder.attributeBlock()
        val attributes = builder.build()
        return htmlElement(false, "a", attributes, block)
    }

    context(_: Bundle)
    fun form(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "form", attributeBlock, block)
    }

    context(_: Bundle)
    fun label(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: HtmlGenericBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "label", attributeBlock, block)
    }

    context(_: Bundle)
    fun input(
        attributeBlock: HtmlInputElementAttributeBuilder.() -> Unit = {},
    ): HtmlElement {
        val builder = HtmlInputElementAttributeBuilder()
        builder.attributeBlock()
        val attributes = builder.build()
        return htmlElement(true, "input", attributes)
    }
}
