package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Node
import dev.scriptor.ui.dom.builder.AttributeBuilder
import dev.scriptor.ui.html.HtmlElement
import dev.scriptor.ui.js.*
import dev.scriptor.ui.js.builder.JsFunctionBuilder

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

    context(bundle: Bundle)
    override fun build(): HtmlElement {
        val element = if (listeners.isEmpty()) {
            HtmlElement(
                void,
                tag,
                attributes,
                children,
            )
        } else {
            val id = bundle.allocateId()

            bundle.script.apply {
                for (listener in listeners) {
                    val options =
                        if (
                            listener.capture == null &&
                            listener.once == null &&
                            listener.passive == null &&
                            listener.signal == null
                        )
                            JsUndefined
                        else
                            JsObject(
                                "capture" to (listener.capture?.let { JsBoolean(listener.capture) } ?: JsUndefined),
                                "once" to (listener.once?.let { JsBoolean(listener.once) } ?: JsUndefined),
                                "passive" to (listener.passive?.let { JsBoolean(listener.passive) } ?: JsUndefined),
                                "signal" to (listener.signal?.let { JsBoolean(listener.signal) } ?: JsUndefined),
                            )

                    val element = document.querySelector("[data-id='$id']")

                    +element["addEventListener"](
                        JsString(listener.type),
                        listener.function,
                        options,
                    )
                }
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
        attributeBlock: AttributeBuilder.() -> Unit = {},
        block: HtmlElementBuilder.() -> Unit = {},
    ): HtmlElement {
        val attributes = AttributeBuilder().apply(attributeBlock).build()
        return element(void, tag, attributes, block)
    }

    context(_: Bundle)
    fun element(
        void: Boolean,
        tag: String,
        attributes: List<Attribute>,
        block: HtmlElementBuilder.() -> Unit = {},
    ): HtmlElement {
        val element = HtmlElementBuilder(void, tag, attributes).apply(block).build()
        children += element
        return element
    }

    fun on(
        type: String,
        capture: Boolean? = null,
        once: Boolean? = null,
        passive: Boolean? = null,
        signal: Boolean? = null,
        block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ) {
        val function = JsFunctionBuilder(
            false,
            null,
            listOf(JsParameter("event", false)),
        ).apply(block).build()

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
