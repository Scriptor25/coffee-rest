package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression

class LockManager(value: JsExpression) : JsProxy(value) {

    val query by proxy("query")
    val request by proxy("request")

    fun query(): JsCall {
        return (query)()
    }

    fun request(name: JsExpression, callback: JsExpression): JsCall {
        return (request)(name, callback)
    }

    fun request(name: JsExpression, options: JsExpression, callback: JsExpression): JsCall {
        return (request)(name, options, callback)
    }
}
