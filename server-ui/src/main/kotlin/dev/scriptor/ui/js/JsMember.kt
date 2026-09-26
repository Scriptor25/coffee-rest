package dev.scriptor.ui.js

open class JsMember(
    val value: JsExpression,
    val name: JsExpression,
) : JsExpression {

    override fun toJsString(statement: Boolean): String {
        val key = when (name) {
            is JsString if validateKey(name.value) -> ".${name.value}"
            else -> "[${name.toJsString(false)}]"
        }

        return "${value.toJsString(false)}$key${if (statement) ";" else ""}"
    }
}
