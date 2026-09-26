package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class ElementProxy(value: JsExpression) : EventTargetProxy(value) {

    val classList by proxy("classList")
}
