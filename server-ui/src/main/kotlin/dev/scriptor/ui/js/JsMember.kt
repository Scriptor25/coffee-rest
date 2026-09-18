package dev.scriptor.ui.js

fun validateKey(key: String): Boolean {
    return """^[A-Za-z_$][A-Za-z0-9_$]*$""".toRegex().matches(key);
}

data class JsMember(
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
