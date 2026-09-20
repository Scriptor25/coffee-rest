package dev.scriptor.ui.js

data class JsIfElse(
    val condition: JsExpression,
    val thenBlock: JsNode,
    val elseBlock: JsNode?,
) : JsNode {

    override fun toJsString(): String =
        if (elseBlock != null)
            "if (${condition.toJsString()}) ${thenBlock.toJsString()} else ${elseBlock.toJsString()}"
        else
            "if (${condition.toJsString()}) ${thenBlock.toJsString()}"
}
