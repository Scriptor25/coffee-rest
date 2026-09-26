package dev.scriptor.ui.js

class JsNew(
    val constructor: JsExpression,
    val arguments: List<JsExpression>,
) : JsExpression {

    override fun toJsString(statement: Boolean): String = "new ${constructor.toJsString(false)}${
        arguments
            .dropLastWhile { it is JsUndefined }
            .joinToString(",", "(", ")") {
                it.toJsString(false)
            }
    }${if (statement) ";" else ""}"
}
