package dev.scriptor.ui.js

data class JsCase(
    val value: JsExpression,
    val nodes: List<JsNode>,
) {

    fun toJsString(): String = buildString {
        append("case ")
        append(value.toJsString(false))
        append(":")

        append(nodes.joinToString("") { it.toJsString(true) })
    }
}
