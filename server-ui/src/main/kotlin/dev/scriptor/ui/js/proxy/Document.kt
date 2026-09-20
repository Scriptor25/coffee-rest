package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString

class Document(value: JsExpression) : JsProxy(value) {

    val querySelector by proxy("querySelector")

    fun querySelector(selectors: String): ElementProxy {
        return ElementProxy((querySelector)(JsString(selectors)))
    }

    fun querySelector(selectors: JsExpression): ElementProxy {
        return ElementProxy((querySelector)(selectors))
    }
}
