package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class DevicePosture(value: JsExpression) : JsProxy(value) {

    val type by proxy("type")
}
