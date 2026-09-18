package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

data object JsWindowProxy : JsSymbol("window") {

    fun fetch(resource: String, options: JsExpression = JsUndefined): JsCall {
        return fetch(JsString(resource), options)
    }

    fun fetch(resource: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return this["fetch"](resource, options)
    }
}
