package dev.scriptor.ui

import dev.scriptor.ui.css.builder.CssNodesBuilder
import dev.scriptor.ui.dom.builder.WithBundle
import dev.scriptor.ui.html.builder.HtmlDocumentBuilder
import dev.scriptor.ui.js.builder.JsNodesBuilder
import kotlin.reflect.KClass

class Bundle internal constructor() {

    private var nextId = 0L

    fun allocateId(): String {
        val id = nextId++
        return "$id"
    }

    private val initialized = mutableSetOf<KClass<out Component>>()

    fun initialize(component: Component) {
        if (component::class in initialized) {
            return
        }

        initialized.add(component::class)

        style.nodes += component.style()
        script.nodes += component.script()
    }

    val document = HtmlDocumentBuilder()
    val script = JsNodesBuilder()
    val style = CssNodesBuilder()

    fun script(block: JsNodesBuilder.() -> Unit) {
        script.apply(block)
    }

    fun style(block: CssNodesBuilder.() -> Unit) {
        style.apply(block)
    }

    fun html(block: WithBundle<HtmlDocumentBuilder>) {
        document.block()
    }

    override fun toString(): String = document.build().toXmlString()
}

fun bundle(block: Bundle.() -> Unit): Bundle {
    return Bundle().apply(block)
}
