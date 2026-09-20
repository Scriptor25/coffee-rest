package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.css.CssNode
import dev.scriptor.ui.css.builder.CssNodesBuilder
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.builder.WithBundle
import dev.scriptor.ui.js.JsNode
import dev.scriptor.ui.js.builder.JsNodesBuilder

open class HtmlGenericBuilder(
    void: Boolean,
    tag: String,
    attributes: List<Attribute>,
) : HtmlElementBuilder(
    void,
    tag,
    attributes,
) {

    context(_: Bundle)
    protected fun htmlElement(
        void: Boolean,
        tag: String,
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(
            void,
            tag,
            HtmlAttributeBuilder().apply(attributeBlock).build(),
            block,
        )
    }

    context(_: Bundle)
    protected fun htmlElement(
        void: Boolean,
        tag: String,
        attributes: List<Attribute>,
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        val builder = HtmlGenericBuilder(
            void,
            tag,
            attributes,
        )
        builder.block()
        children += builder.build()
    }

    context(_: Bundle)
    fun script(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: JsNodesBuilder.() -> Unit = {},
    ) {
        val script = JsNodesBuilder().apply(block).build()
        val source = script.joinToString(";", transform = JsNode::toJsString)

        htmlElement(
            false,
            "script",
            {
                attributeBlock()
                this["type"] = "module"
            },
        ) { raw(source) }
    }

    context(_: Bundle)
    fun style(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: CssNodesBuilder.() -> Unit = {},
    ) {
        val styles = CssNodesBuilder().apply(block).build()
        val source = styles.joinToString("", transform = CssNode::toCssString)

        htmlElement(
            false,
            "style",
            attributeBlock,
        ) { raw(source) }
    }

    context(_: Bundle)
    fun main(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "main", attributeBlock, block)
    }

    context(_: Bundle)
    fun div(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "div", attributeBlock, block)
    }

    context(_: Bundle)
    fun span(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "span", attributeBlock, block)
    }

    context(_: Bundle)
    fun p(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "p", attributeBlock, block)
    }

    context(_: Bundle)
    fun h1(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "h1", attributeBlock, block)
    }

    context(_: Bundle)
    fun h2(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "h2", attributeBlock, block)
    }

    context(_: Bundle)
    fun h3(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "h3", attributeBlock, block)
    }

    context(_: Bundle)
    fun h4(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "h4", attributeBlock, block)
    }

    context(_: Bundle)
    fun h5(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "h5", attributeBlock, block)
    }

    context(_: Bundle)
    fun h6(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "h6", attributeBlock, block)
    }

    context(_: Bundle)
    fun button(
        attributeBlock: HtmlButtonElementAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        val attributes = HtmlButtonElementAttributeBuilder().apply(attributeBlock).build()
        htmlElement(false, "button", attributes, block)
    }

    context(_: Bundle)
    fun section(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "section", attributeBlock, block)
    }

    context(_: Bundle)
    fun blockquote(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "blockquote", attributeBlock, block)
    }

    context(_: Bundle)
    fun cite(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "cite", attributeBlock, block)
    }

    context(_: Bundle)
    fun ul(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "ul", attributeBlock, block)
    }

    context(_: Bundle)
    fun ol(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "ol", attributeBlock, block)
    }

    context(_: Bundle)
    fun li(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "li", attributeBlock, block)
    }

    context(_: Bundle)
    fun a(
        attributeBlock: HtmlAnchorElementAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        val attributes = HtmlAnchorElementAttributeBuilder().apply(attributeBlock).build()
        htmlElement(false, "a", attributes, block)
    }

    context(_: Bundle)
    fun form(
        attributeBlock: HtmlFormElementAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        val attributes = HtmlFormElementAttributeBuilder().apply(attributeBlock).build()
        htmlElement(false, "form", attributes, block)
    }

    context(_: Bundle)
    fun label(
        attributeBlock: HtmlAttributeBuilder.() -> Unit = {},
        block: WithBundle<HtmlGenericBuilder> = {},
    ) {
        htmlElement(false, "label", attributeBlock, block)
    }

    context(_: Bundle)
    fun input(
        attributeBlock: HtmlInputElementAttributeBuilder.() -> Unit = {},
    ) {
        val attributes = HtmlInputElementAttributeBuilder().apply(attributeBlock).build()
        htmlElement(true, "input", attributes)
    }
}
