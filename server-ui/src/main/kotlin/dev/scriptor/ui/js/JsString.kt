package dev.scriptor.ui.js

fun escapeString(str: String): String = buildString {
    for (char in str) {
        if (char != '"') {
            append(char)
            continue
        }

        append("\\\"")
    }
}

class JsString(
    val value: String,
) : JsExpression {

    // TODO: escape string value
    override fun toJsString(): String = """"${escapeString(value)}""""
}
