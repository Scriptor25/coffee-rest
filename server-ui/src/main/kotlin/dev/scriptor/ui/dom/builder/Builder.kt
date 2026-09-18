package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.*

interface Builder<T> {

    val children: MutableList<Node>

    context(bundle: Bundle)
    fun build(): T

    fun raw(content: String): Raw {
        val node = Raw(content)
        children += node
        return node
    }

    fun text(content: String): Text {
        val node = Text(content)
        children += node
        return node
    }

    fun entity(name: String): Entity {
        val node = Entity(name)
        children += node
        return node
    }

    fun comment(content: String): Comment {
        val node = Comment(content)
        children += node
        return node
    }

    context(_: Bundle)
    fun element(tag: String, vararg attributes: Pair<String, String?>, block: ElementBuilder.() -> Unit = {}): Element {
        val builder = ElementBuilder(tag, attributes.map { Attribute(it.first, it.second) })
        builder.block()
        val node = builder.build()
        children += node
        return node
    }

    operator fun String.unaryPlus(): Text {
        return text(this)
    }
}
