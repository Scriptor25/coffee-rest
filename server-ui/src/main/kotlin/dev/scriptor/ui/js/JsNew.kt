package dev.scriptor.ui.js

class JsNew(
    val constructor: JsExpression,
    val arguments: List<JsExpression>,
) : JsExpression {

    override fun toJsString(): String = "new ${constructor.toJsString()}${
        arguments
            .dropLastWhile { it is JsUndefined }
            .joinToString(
                ",",
                "(",
                ")",
                transform = JsExpression::toJsString,
            )
    }"
}
