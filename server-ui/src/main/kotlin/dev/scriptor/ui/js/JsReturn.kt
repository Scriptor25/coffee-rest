package dev.scriptor.ui.js

class JsReturn(val value: JsExpression) : JsNode {

    override fun toJsString(): String = "return ${value.toJsString()}"
}
