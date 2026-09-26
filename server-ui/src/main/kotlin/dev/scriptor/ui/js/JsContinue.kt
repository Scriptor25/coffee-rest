package dev.scriptor.ui.js

data object JsContinue : JsNode {

    override fun toJsString(statement: Boolean): String =
        "continue${if (statement) ";" else ""}"
}
