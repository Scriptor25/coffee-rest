package dev.scriptor.ui.js

class JsString(
    val value: String,
) : JsExpression {

    override fun toJsString(statement: Boolean): String =
        if (statement) ""
        else "\"${escapeString(value, '"')}\""
}
