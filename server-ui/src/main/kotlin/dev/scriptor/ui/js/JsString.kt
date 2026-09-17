package dev.scriptor.ui.js

class JsString(
    val value: String,
) : JsExpression {

    // TODO: escape string value
    override fun toJsString(): String = """"$value""""
}
