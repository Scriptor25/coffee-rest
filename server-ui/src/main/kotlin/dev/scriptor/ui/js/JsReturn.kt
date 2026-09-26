package dev.scriptor.ui.js

class JsReturn(val value: JsExpression) : JsNode {

    override fun toJsString(statement: Boolean): String {
        val string = when (value) {
            JsUndefined -> "return"
            else -> "return ${value.toJsString(false)}"
        }

        return "$string${if (statement) ";" else ""}"
    }
}
