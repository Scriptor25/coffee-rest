package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

open class JsProxy(
    val value: JsExpression,
) : JsExpression {

    override fun toJsString(): String = value.toJsString()
}
