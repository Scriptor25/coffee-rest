package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Node
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.*
import dev.scriptor.ui.js.builder.JsFunctionBuilder

typealias Component = (List<Attribute>, List<Node>) -> HtmlElement

/**
 * `addEventListener(type, function, { capture, once, passive, signal })`
 */
data class HtmlEventListener(
    val type: String,
    val function: JsFunction,
    val capture: Boolean? = null,
    val once: Boolean? = null,
    val passive: Boolean? = null,
    val signal: Boolean? = null,
)

open class HtmlElementBuilder(
    val void: Boolean,
    val tag: String,
    val attributes: List<Attribute>,
) : HtmlBuilder<HtmlElement> {

    override val children = mutableListOf<Node>()

    val listeners = mutableListOf<HtmlEventListener>()

    context(context: Bundle)
    override fun build(): HtmlElement {
        val element = if (listeners.isEmpty()) {
            HtmlElement(
                void,
                tag,
                attributes,
                children,
            )
        } else {
            val id = context.allocateId()

            for (listener in listeners) {
                val document = JsSymbol("document")
                val querySelector = JsMember(document, "querySelector")
                val element = JsCall(querySelector, listOf(JsString("[data-id='$id']")))
                val addEventListener = JsMember(element, "addEventListener")

                context.script.call(
                    addEventListener,
                    JsString(listener.type),
                    listener.function,
                    JsObject(
                        "capture" to (listener.capture?.let { JsBoolean(listener.capture) } ?: JsUndefined),
                        "once" to (listener.once?.let { JsBoolean(listener.once) } ?: JsUndefined),
                        "passive" to (listener.passive?.let { JsBoolean(listener.passive) } ?: JsUndefined),
                        "signal" to (listener.signal?.let { JsBoolean(listener.signal) } ?: JsUndefined),
                    ),
                )
            }

            HtmlElement(
                void,
                tag,
                attributes + Attribute("data-id", id),
                children,
            )
        }

        return element
    }

    context(_: Bundle)
    fun element(
        void: Boolean,
        tag: String,
        vararg attributes: Pair<String, String?>,
        block: HtmlElementBuilder.() -> Unit
    ): HtmlElement {
        return element(void, tag, attributes.map { Attribute(it.first, it.second) }, block)
    }

    context(_: Bundle)
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
        capture: Boolean? = null,
        once: Boolean? = null,
        passive: Boolean? = null,
        signal: Boolean? = null,
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
