package dev.scriptor.ui.js

data class JsSymbol(
    val name: String,
) : JsExpression {

    // TODO: sanitize symbol name
    override fun toJsString(): String = name
}
