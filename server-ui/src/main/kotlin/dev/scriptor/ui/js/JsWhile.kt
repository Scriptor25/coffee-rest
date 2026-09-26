package dev.scriptor.ui.js

data class JsWhile(
    val condition: JsExpression,
    val node: JsNode,
) : JsNode {

    override fun toJsString(statement: Boolean): String =
        "while (${condition.toJsString(false)}) ${node.toJsString(true)}"
}
