package dev.scriptor.ui.js

class JsString(
    val value: String,
) : JsExpression {

    override fun toJsString(): String = "\"${escapeString(value, '"')}\""
}
