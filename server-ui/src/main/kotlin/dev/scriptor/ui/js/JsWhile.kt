package dev.scriptor.ui.js

data class JsWhile(
    val condition: JsExpression,
    val node: JsNode,
) : JsNode {

    override fun toJsString(): String =
        "while (${condition.toJsString()}) ${node.toJsString()}"
}
