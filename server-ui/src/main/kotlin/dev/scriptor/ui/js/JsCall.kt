package dev.scriptor.ui.js

data class JsCall(
    val callee: JsExpression,
    val arguments: List<JsExpression>,
) : JsExpression {

    override fun toJsString(statement: Boolean): String =
        "${callee.toJsString(false)}${
            arguments
                .dropLastWhile { it is JsUndefined }
                .joinToString(",", "(", ")") {
                    it.toJsString(false)
                }
        }${if (statement) ";" else ""}"
}
