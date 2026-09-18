package dev.scriptor.ui.js

data object JsUndefined : JsExpression {

    override fun toJsString(): String = "undefined"
}
