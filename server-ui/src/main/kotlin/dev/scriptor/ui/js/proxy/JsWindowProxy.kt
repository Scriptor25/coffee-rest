package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString
import dev.scriptor.ui.js.JsUndefined

class JsWindowProxy(value: JsExpression) : JsProxy(value) {

    val location = JsLocationProxy(this["location"])
    val navigator = JsNavigatorProxy(this["navigator"])

    fun fetch(resource: String, options: JsExpression = JsUndefined): JsCall {
        return fetch(JsString(resource), options)
    }

    fun fetch(resource: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return this["fetch"](resource, options)
    }

    fun open(url: JsExpression): JsCall {
        return this["open"](url)
    }
}
