package dev.scriptor.ui.js

class JsThrow(val value: JsExpression) : JsNode {

    override fun toJsString(): String = "throw ${value.toJsString()}"
}
