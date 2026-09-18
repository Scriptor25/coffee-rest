package dev.scriptor.ui.js

data class JsIf(
    val condition: JsExpression,
    val thenBlock: JsNode,
    val elseBlock: JsNode,
) : JsNode {

    override fun toJsString(): String =
        "if (${condition.toJsString()}) ${thenBlock.toJsString()} else ${elseBlock.toJsString()}"
}
