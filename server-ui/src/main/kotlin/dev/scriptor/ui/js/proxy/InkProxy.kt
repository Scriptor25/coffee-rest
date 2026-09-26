package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class InkProxy(value: JsExpression) : JsProxy(value) {

    val requestPresenter by proxy("requestPresenter")

    fun requestPresenter(param: JsExpression = JsUndefined): JsCall {
        return (requestPresenter)(param)
    }
}
