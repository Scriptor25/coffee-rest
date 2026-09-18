package dev.scriptor.ui.js

data class JsNumber(val value: Number) : JsExpression {

    override fun toJsString(): String = "$value"
}