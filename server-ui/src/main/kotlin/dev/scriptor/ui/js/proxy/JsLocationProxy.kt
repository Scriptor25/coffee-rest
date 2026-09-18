package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class JsLocationProxy(value: JsExpression) : JsProxy(value) {

    val origin: JsExpression
        get() = this["origin"]
}
