package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class GPU(value: JsExpression) : JsProxy(value) {

    val getPreferredCanvasFormat by proxy("getPreferredCanvasFormat")
    val requestAdapter by proxy("requestAdapter")
    val wgslLanguageFeatures by proxy("wgslLanguageFeatures", ::WGSLLanguageFeatures)

    fun getPreferredCanvasFormat(): JsCall {
        return (getPreferredCanvasFormat)()
    }

    fun requestAdapter(options: JsExpression = JsUndefined): JsCall {
        return (requestAdapter)(options)
    }
}
