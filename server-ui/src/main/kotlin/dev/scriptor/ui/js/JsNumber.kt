package dev.scriptor.ui.js

data class JsNumber(val value: Number) : JsExpression {

    override fun toJsString(statement: Boolean): String =
        if (statement) ""
        else "$value"
}