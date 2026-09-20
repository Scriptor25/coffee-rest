package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression

class NetworkInformation(value: JsExpression) : JsProxy(value) {

    val downlink by proxy("downlink")
    val downlinkMax by proxy("downlinkMax")
    val effectiveType by proxy("effectiveType")
    val rtt by proxy("rtt")
    val saveData by proxy("saveData")
    val type by proxy("type")
}
