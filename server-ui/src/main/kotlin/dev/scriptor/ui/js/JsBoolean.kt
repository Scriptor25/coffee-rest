package dev.scriptor.ui.js

data class JsBoolean(val value: Boolean) : JsExpression {

    override fun toJsString(): String = if (value) "true" else "false"
}