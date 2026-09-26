package dev.scriptor.ui.js

open class JsSymbol(
    val name: String,
) : JsExpression {

    override fun toJsString(statement: Boolean): String {
        if (statement)
            return ""

        require(validateKey(name))
        return name
    }
}
