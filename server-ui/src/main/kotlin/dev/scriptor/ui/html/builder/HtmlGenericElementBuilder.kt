package dev.scriptor.ui.html.builder

import dev.scriptor.ui.BuilderContext
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.html.HtmlElement

open class HtmlGenericElementBuilder(
    void: Boolean,
    tag: String,
    attributes: List<Attribute>,
) : HtmlElementBuilder(void, tag, attributes) {

    context(_: BuilderContext)
    fun htmlElement(
        void: Boolean,
        tag: String,
        vararg attributes: Pair<String, String?>,
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(void, tag, attributes.map { Attribute(it.first, it.second) }, block)
    }

    context(_: BuilderContext)
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

    context(_: BuilderContext)
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

    context(_: BuilderContext)
    fun main(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "main", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun div(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "div", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun span(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "span", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun p(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "p", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun h1(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h1", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun h2(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h2", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun h3(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h3", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun h4(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h4", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun h5(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h5", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun h6(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "h6", attributeBlock, block)
    }

    context(_: BuilderContext)
    fun button(
        attributeBlock: HtmlGlobalAttributeBuilder.() -> Unit = {},
        block: HtmlGenericElementBuilder.() -> Unit = {},
    ): HtmlElement {
        return htmlElement(false, "button", attributeBlock, block)
    }
}
