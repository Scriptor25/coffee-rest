package dev.scriptor.ui.js

data class JsArray(
    val elements: List<JsExpression>,
) : JsExpression {

    override fun toJsString(): String =
        elements.joinToString(
            ",",
            "[",
            "]",
            transform = JsExpression::toJsString,
        )
}
