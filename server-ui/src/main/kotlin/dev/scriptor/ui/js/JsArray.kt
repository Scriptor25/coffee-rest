package dev.scriptor.ui.js

data class JsArray(
    val elements: List<JsExpression>,
) : JsExpression {

    override fun toJsString(statement: Boolean): String {
        val string = elements.joinToString(",", "[", "]") {
            it.toJsString(false)
        }

        return "$string${if (statement) ";" else ""}"
    }
}
