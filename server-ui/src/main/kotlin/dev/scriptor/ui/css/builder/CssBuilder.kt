package dev.scriptor.ui.css.builder

import dev.scriptor.ui.css.CssNode

abstract class CssBuilder<T> {

    val nodes = mutableListOf<CssNode>()

    abstract fun build(): T
}
