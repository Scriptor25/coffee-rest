package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

open class EventTargetProxy(value: JsExpression) : JsProxy(value) {

    val addEventListener by proxy("addEventListener")
    val removeEventListener by proxy("removeEventListener")
    val dispatchEvent by proxy("dispatchEvent")

    fun addEventListener(type: JsExpression, listener: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return (addEventListener)(type, listener, options)
    }

    fun removeEventListener(type: JsExpression, listener: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return (removeEventListener)(type, listener, options)
    }

    fun dispatchEvent(event: JsExpression): JsCall {
        return (dispatchEvent)(event)
    }
}
