package dev.scriptor.ui.js

data class JsCall(
    val callee: JsExpression,
    val arguments: List<JsExpression>,
) : JsExpression {

    override fun toJsString(): String =
        "${callee.toJsString()}${
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
