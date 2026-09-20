package dev.scriptor.ui.js

data object JsNull : JsExpression {

    override fun toJsString(): String = "null"
}
