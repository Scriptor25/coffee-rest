package dev.scriptor.ui.css.builder

import dev.scriptor.ui.css.CssClass
import dev.scriptor.ui.css.CssNode

abstract class CssBuilder<T> {

    val nodes = mutableListOf<CssNode>()

    abstract fun build(): T

    fun define(selector: String, block: CssClassBuilder.() -> Unit): CssClass {
        val builder = CssClassBuilder(selector)
        builder.block()
        val node = builder.build()
        nodes += node
        return node
    }
}
