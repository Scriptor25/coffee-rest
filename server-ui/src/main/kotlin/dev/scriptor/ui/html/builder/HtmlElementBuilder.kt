package dev.scriptor.ui.html.builder

import dev.scriptor.ui.BuilderContext
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Node
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.JsFunction
import dev.scriptor.ui.js.JsParameter
import dev.scriptor.ui.js.builder.JsFunctionBuilder

typealias Component = (List<Attribute>, List<Node>) -> HtmlElement

/**
 * `addEventListener(type, function, { capture, once, passive, signal })`
 */
data class HtmlEventListener(
    val type: String,
    val function: JsFunction,
    val capture: Boolean,
    val once: Boolean,
    val passive: Boolean,
    val signal: Boolean,
)

open class HtmlElementBuilder(
    val void: Boolean,
    val tag: String,
    val attributes: List<Attribute>,
) : HtmlBuilder<HtmlElement> {

    override val children = mutableListOf<Node>()

    val listeners = mutableListOf<HtmlEventListener>()

    context(context: BuilderContext)
    override fun build(): HtmlElement {
        return HtmlElement(void, tag, attributes, children)
    }

    context(_: BuilderContext)
    fun element(
        void: Boolean,
        tag: String,
        vararg attributes: Pair<String, String?>,
        block: HtmlElementBuilder.() -> Unit
    ): HtmlElement {
        return element(void, tag, attributes.map { Attribute(it.first, it.second) }, block)
    }

    context(_: BuilderContext)
    fun element(
        void: Boolean,
        tag: String,
        attributes: List<Attribute>,
        block: HtmlElementBuilder.() -> Unit
    ): HtmlElement {
        val builder = HtmlElementBuilder(void, tag, attributes)
        builder.apply(block)
        val element = builder.build()
        children += element
        return element
    }

    fun on(
        type: String,
        capture: Boolean = false,
        once: Boolean = false,
        passive: Boolean = false,
        signal: Boolean = false,
        block: JsFunctionBuilder.() -> Unit,
    ) {
        val builder = JsFunctionBuilder(
            false,
            null,
            listOf(JsParameter("event", false)),
        )
        builder.apply(block)
        val function = builder.build()
        listeners += HtmlEventListener(
            type,
            function,
            capture,
            once,
            passive,
            signal,
        )
    }
}
