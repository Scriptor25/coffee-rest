package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.*

typealias WithBundle<B> = context(Bundle) B.() -> Unit

interface Builder<T> {

    val children: MutableList<Node>

    context(bundle: Bundle)
    fun build(): T

    fun raw(content: String) {
        children += Raw(content)
    }

    fun text(content: String) {
        children += Text(content)
    }

    fun entity(name: String) {
        children += Entity(name)
    }

    fun comment(content: String) {
        children += Comment(content)
    }

    context(_: Bundle)
    fun element(
        tag: String,
        vararg attributes: Pair<String, AttributeValue>,
        block: WithBundle<ElementBuilder> = {}
    ) {
        val builder = ElementBuilder(tag, attributes.map(::Attribute))
        builder.block()
        children += builder.build()
    }

    operator fun String.unaryPlus() {
        text(this)
    }

    operator fun Node?.unaryPlus() {
        if (this != null) {
            children += this
        }
    }

    operator fun Iterable<Node>.unaryPlus() {
        children += this
    }
}
