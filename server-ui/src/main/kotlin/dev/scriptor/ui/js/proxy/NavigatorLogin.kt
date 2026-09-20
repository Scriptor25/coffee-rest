package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression

class NavigatorLogin(value: JsExpression) : JsProxy(value) {

    val setStatus by proxy("setStatus")

    fun setStatus(status: JsExpression): JsCall {
        return (setStatus)(status)
    }
}
