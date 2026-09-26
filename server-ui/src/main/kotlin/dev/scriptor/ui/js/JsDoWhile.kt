package dev.scriptor.ui.js

data class JsDoWhile(
    val condition: JsExpression,
    val node: JsNode,
) : JsNode {

    override fun toJsString(statement: Boolean): String =
        "do ${node.toJsString(true)} while (${condition.toJsString(false)})"
}
