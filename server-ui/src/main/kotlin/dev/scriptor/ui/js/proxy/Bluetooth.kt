package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class Bluetooth(value: JsExpression) : JsProxy(value) {

    val getAvailability by proxy("getAvailability")
    val getDevices by proxy("getDevices")
    val requestDevice by proxy("requestDevice")

    fun getAvailability(): JsCall {
        return (getAvailability)()
    }

    fun getDevices(): JsCall {
        return (getDevices)()
    }

    fun requestDevice(options: JsExpression = JsUndefined): JsCall {
        return (requestDevice)(options)
    }
}
