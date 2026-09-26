package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class KeyboardProxy(value: JsExpression) : JsProxy(value) {

    val getLayoutMap by proxy("getLayoutMap")
    val lock by proxy("lock")
    val unlock by proxy("unlock")

    fun getLayoutMap(): JsCall {
        return (getLayoutMap)()
    }

    fun lock(keyCodes: JsExpression = JsUndefined): JsCall {
        return (lock)(keyCodes)
    }

    fun unlock(): JsCall {
        return (unlock)()
    }
}
