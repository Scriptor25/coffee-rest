package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class AudioSession(value: JsExpression) : JsProxy(value) {

    val state by proxy("state")
    var type by proxy("type")
}
