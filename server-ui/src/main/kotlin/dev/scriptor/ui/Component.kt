package dev.scriptor.ui

import dev.scriptor.ui.css.CssNode
import dev.scriptor.ui.dom.Node
import dev.scriptor.ui.js.JsNode

interface Component {

    context(bundle: Bundle)
    fun build(): Node? = null

    context(bundle: Bundle)
    fun buildFragment(): List<Node> = listOfNotNull(build())

    fun style(): List<CssNode> = emptyList()
    fun script(): List<JsNode> = emptyList()
}

context(bundle: Bundle)
fun <T : Component> component(factory: () -> T, block: T.() -> Unit): List<Node> {
    val component = factory().apply(block)

    bundle.initialize(component)

    return component.buildFragment()
}
