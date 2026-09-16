package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsNode

interface JsBuilder<T> {

    val nodes: MutableList<JsNode>

    fun build(): T
}
