package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression

class JsClipboardProxy(value: JsExpression) : JsProxy(value) {

    fun writeText(text: JsExpression): JsCall {
        return this["writeText"](text)
    }
}
