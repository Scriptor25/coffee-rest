package dev.scriptor.ui.js

data class JsBoolean(val value: Boolean) : JsExpression {

    override fun toJsString(statement: Boolean): String =
        if (statement) ""
        else "$value"
}
