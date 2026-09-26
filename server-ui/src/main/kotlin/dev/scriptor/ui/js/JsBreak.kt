package dev.scriptor.ui.js

data object JsBreak : JsNode {

    override fun toJsString(statement: Boolean): String =
        "break${if (statement) ";" else ""}"
}
