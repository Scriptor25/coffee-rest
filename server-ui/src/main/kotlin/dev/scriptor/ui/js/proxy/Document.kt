package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString

class Document(value: JsExpression) : JsProxy(value) {

    val querySelector by proxy("querySelector")

    fun querySelector(selectors: String): JsCall {
        return (querySelector)(JsString(selectors))
    }

    fun querySelector(selectors: JsExpression): JsCall {
        return (querySelector)(selectors)
    }
}
