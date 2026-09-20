package dev.scriptor.ui.js

class JsReturn(val value: JsExpression) : JsNode {

    override fun toJsString(): String =
        when (value) {
            JsUndefined -> "return"
            else -> "return ${value.toJsString()}"
        }
}
