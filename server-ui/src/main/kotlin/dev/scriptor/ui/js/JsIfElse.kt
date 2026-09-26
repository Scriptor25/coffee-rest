package dev.scriptor.ui.js

data class JsIfElse(
    val condition: JsExpression,
    val thenBlock: JsNode,
    val elseBlock: JsNode?,
) : JsNode {

    override fun toJsString(statement: Boolean): String =
        if (elseBlock != null)
            "if (${condition.toJsString(false)}) ${thenBlock.toJsString(true)} else ${elseBlock.toJsString(false)}"
        else
            "if (${condition.toJsString(false)}) ${thenBlock.toJsString(true)}"
}
