package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class AudioSessionProxy(value: JsExpression) : JsProxy(value) {

    val state by proxy("state")
    val type by proxy("type")
}
