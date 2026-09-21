package dev.scriptor.ui.js

class JsFormat(
    val strings: List<String>,
    val values: List<JsExpression>,
) : JsExpression {

    override fun toJsString(): String =
        "`${
            buildString {
                val count = minOf(strings.size, values.size)
                for (i in 0 until count) {
                    append(escapeString(strings[i], '`'))
                    append($$"${$${values[i].toJsString()}}")
                }
                for (i in count until strings.size) {
                    append(escapeString(strings[i], '`'))
                }
                for (i in count until values.size) {
                    append($$"${$${values[i].toJsString()}}")
                }
            }
        }`"
}
