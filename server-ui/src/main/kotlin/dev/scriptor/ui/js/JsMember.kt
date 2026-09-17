package dev.scriptor.ui.js

data class JsMember(
    val value: JsExpression,
    val name: String,
) : JsExpression {

    // TODO: sanitize name, use `[...]` accessor instead of dot notation for non-sane names
    override fun toJsString(): String = "${value.toJsString()}.$name"
}
