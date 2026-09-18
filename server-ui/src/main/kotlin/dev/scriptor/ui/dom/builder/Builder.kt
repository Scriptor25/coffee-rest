package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.*

interface Builder<T> {

    val children: MutableList<Node>

    context(bundle: Bundle)
    fun build(): T

    fun text(content: String): Text {
        val text = Text(content)
        children += text
        return text
    }

    fun comment(content: String): Comment {
        val comment = Comment(content)
        children += comment
        return comment
    }

    context(_: Bundle)
    fun element(tag: String, vararg attributes: Pair<String, String?>, block: ElementBuilder.() -> Unit = {}): Element {
        val builder = ElementBuilder(tag, attributes.map { Attribute(it.first, it.second) })
        builder.apply(block)
        val element = builder.build()
        children += element
        return element
    }

    fun entity(name: String): Entity {
        val entity = Entity(name)
        children += entity
        return entity
    }

    operator fun String.unaryPlus(): Text {
        return text(this)
    }
}
