package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

data class JsForEachBuilder(
    val kind: JsForEachKind,
    val iteratorKind: JsVariableKind,
    val name: String,
    val value: JsExpression,
) : JsNodeBuilder<JsForEach>() {

    override fun build(): JsForEach {
        val node = if (nodes.size == 1) nodes[0] else JsBlock(nodes)

        return JsForEach(
            kind,
            iteratorKind,
            name,
            value,
            node,
        )
    }

    fun apply(block: JsForEachBuilder.(JsSymbol) -> Unit): JsForEachBuilder {
        block(JsSymbol(name))
        return this
    }
}