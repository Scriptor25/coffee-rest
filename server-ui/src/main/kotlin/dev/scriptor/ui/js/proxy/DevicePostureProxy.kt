package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class DevicePostureProxy(value: JsExpression) : JsProxy(value) {

    val type by proxy("type")
}
