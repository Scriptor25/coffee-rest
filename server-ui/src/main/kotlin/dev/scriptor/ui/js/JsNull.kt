package dev.scriptor.ui.js

data object JsNull : JsExpression {

    override fun toJsString(statement: Boolean): String =
        if (statement) ""
        else "null"
}
