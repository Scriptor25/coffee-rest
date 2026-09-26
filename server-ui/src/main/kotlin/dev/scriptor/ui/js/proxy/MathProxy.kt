package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression

class MathProxy(value: JsExpression) : JsProxy(value) {

    val abs by proxy("abs")
    val hypot by proxy("hypot")
    val sign by proxy("sign")

    fun abs(x: JsExpression): JsCall {
        return (abs)(x)
    }

    fun hypot(x: JsExpression, y: JsExpression): JsCall {
        return (hypot)(x, y)
    }

    fun sign(x: JsExpression): JsCall {
        return (sign)(x)
    }
}
