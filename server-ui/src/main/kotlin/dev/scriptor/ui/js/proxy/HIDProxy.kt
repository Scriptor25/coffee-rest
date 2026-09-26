package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression

class HIDProxy(value: JsExpression) : JsProxy(value) {

    val getDevices by proxy("getDevices")
    val requestDevice by proxy("requestDevice")

    fun getDevices(): JsCall {
        return (getDevices)()
    }

    fun requestDevice(options: JsExpression): JsCall {
        return (requestDevice)(options)
    }
}
