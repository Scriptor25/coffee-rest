package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString

class JsDocumentProxy(value: JsExpression) : JsProxy(value) {

    fun querySelector(selectors: String): JsCall {
        return querySelector(JsString(selectors))
    }

    fun querySelector(selectors: JsExpression): JsCall {
        return this["querySelector"](selectors)
    }
}
