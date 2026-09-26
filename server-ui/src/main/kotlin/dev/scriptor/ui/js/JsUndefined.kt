package dev.scriptor.ui.js

data object JsUndefined : JsExpression {

    override fun toJsString(statement: Boolean): String =
        if (statement) ""
        else "undefined"
}
