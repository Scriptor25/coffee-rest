package dev.scriptor.ui

import dev.scriptor.ui.css.builder.CssNodesBuilder
import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.dom.builder.DocumentBuilder
import dev.scriptor.ui.dom.builder.WithBundle
import dev.scriptor.ui.html.builder.HtmlDocumentBuilder
import dev.scriptor.ui.js.builder.JsNodesBuilder
import kotlin.reflect.KClass

class Bundle {

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

    val script = JsNodesBuilder()
    val style = CssNodesBuilder()

    fun script(block: JsNodesBuilder.() -> Unit) {
        script.apply(block)
    }

    fun style(block: CssNodesBuilder.() -> Unit) {
        style.apply(block)
    }

    fun document(type: String, block: WithBundle<DocumentBuilder>): Document {
        val builder = DocumentBuilder(type)
        builder.block()
        return builder.build()
    }

    fun html(block: WithBundle<HtmlDocumentBuilder>): Document {
        val builder = HtmlDocumentBuilder()
        builder.block()
        return builder.build()
    }
}

fun bundle(block: Bundle.() -> Unit): Bundle {
    return Bundle().apply(block)
}
