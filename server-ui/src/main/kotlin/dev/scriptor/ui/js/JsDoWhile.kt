package dev.scriptor.ui.js

data class JsDoWhile(
    val condition: JsExpression,
    val node: JsNode,
) : JsNode {

    override fun toJsString(): String =
        "do ${node.toJsString()} while (${condition.toJsString()})"
}
