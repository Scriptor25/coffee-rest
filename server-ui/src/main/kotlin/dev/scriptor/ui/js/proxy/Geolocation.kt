package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class Geolocation(value: JsExpression) : JsProxy(value) {

    val clearWatch by proxy("clearWatch")
    val getCurrentPosition by proxy("getCurrentPosition")
    val watchPosition by proxy("watchPosition")

    fun clearWatch(id: JsExpression): JsCall {
        return (clearWatch)(id)
    }

    fun getCurrentPosition(
        success: JsExpression,
        error: JsExpression = JsUndefined,
        options: JsExpression = JsUndefined,
    ): JsCall {
        return (getCurrentPosition)(success, error, options)
    }

    fun watchPosition(
        success: JsExpression,
        error: JsExpression = JsUndefined,
        options: JsExpression = JsUndefined,
    ): JsCall {
        return (watchPosition)(success, error, options)
    }
}
