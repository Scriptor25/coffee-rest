package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.*

typealias WithBundle<B> = context(Bundle) B.() -> Unit

interface Builder<T> {

    val children: MutableList<Node>

    context(bundle: Bundle)
    fun build(): T

    fun <N : Node> add(node: N): N {
        children += node
        return node
    }

    fun raw(content: String): Raw {
        return add(Raw(content))
    }

    fun text(content: String): Text {
        return add(Text(content))
    }

    fun entity(name: String): Entity {
        return add(Entity(name))
    }

    fun comment(content: String): Comment {
        return add(Comment(content))
    }

    context(_: Bundle)
    fun element(
        tag: String,
        vararg attributes: Pair<String, AttributeValue>,
        block: WithBundle<ElementBuilder> = {}
    ): Element {
        val builder = ElementBuilder(tag, attributes.map(::Attribute))
        builder.block()
        val element = builder.build()
        return add(element)
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
