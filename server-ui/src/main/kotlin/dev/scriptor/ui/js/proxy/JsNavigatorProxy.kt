package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class JsNavigatorProxy(value: JsExpression) : JsProxy(value) {

    val share: JsExpression
        get() = this["share"]

    val clipboard = JsClipboardProxy(this["clipboard"])
}
