package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

abstract class JsSetProxy(value: JsExpression) : JsProxy(value) {

    val entries by proxy("entries")
    val forEach by proxy("forEach")
    val has by proxy("has")
    val keys by proxy("keys")
    val size by proxy("size")
    val values by proxy("values")

    fun entries(): JsCall {
        return (entries)()
    }

    fun forEach(callbackFn: JsExpression, thisArg: JsExpression = JsUndefined): JsCall {
        return (forEach)(callbackFn, thisArg)
    }

    fun has(value: JsExpression): JsCall {
        return (has)(value)
    }

    fun keys(): JsCall {
        return (keys)()
    }

    fun values(): JsCall {
        return (values)()
    }
}
