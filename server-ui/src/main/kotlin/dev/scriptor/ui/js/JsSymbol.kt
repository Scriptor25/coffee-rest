package dev.scriptor.ui.js

open class JsSymbol(
    val name: String,
) : JsExpression {

    override fun toJsString(): String {
        require(validateKey(name))

        return name
    }
}
