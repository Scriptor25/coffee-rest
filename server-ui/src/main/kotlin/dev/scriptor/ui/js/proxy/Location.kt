package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class Location(value: JsExpression) : JsProxy(value) {

    val origin by proxy("origin")
}
