package dev.scriptor.ui.js

class JsThrow(val value: JsExpression) : JsNode {

    override fun toJsString(statement: Boolean): String =
        "throw ${value.toJsString(false)}${if (statement) ";" else ""}"
}
