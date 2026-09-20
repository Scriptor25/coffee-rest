package dev.scriptor.ui.js

open class JsMember(
    val value: JsExpression,
    val name: JsExpression,
) : JsExpression {

    override fun toJsString(): String {
        val key = when (name) {
            is JsString if validateKey(name.value) -> ".${name.value}"
            else -> "[${name.toJsString()}]"
        }

        return "${value.toJsString()}$key"
    }
}
