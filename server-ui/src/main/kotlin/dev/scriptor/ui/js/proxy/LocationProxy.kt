package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class LocationProxy(value: JsExpression) : JsProxy(value) {

    val origin by proxy("origin")
}
