package dev.scriptor.ui.js

data class JsTernary(
    val condition: JsExpression,
    val thenValue: JsExpression,
    val elseValue: JsExpression,
) : JsExpression {

    override fun toJsString(statement: Boolean): String =
        "(${condition.toJsString(false)} ? ${thenValue.toJsString(false)} : ${elseValue.toJsString(false)})${if (statement) ";" else ""}"
}
