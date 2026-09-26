package dev.scriptor.ui.js

data class JsTernary(
    val condition: JsExpression,
    val thenValue: JsExpression,
    val elseValue: JsExpression,
) : JsExpression {

    override fun toJsString(): String =
        "(${condition.toJsString()} ? ${thenValue.toJsString()} : ${elseValue.toJsString()})"
}
