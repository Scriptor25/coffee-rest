package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class HistoryProxy(value: JsExpression) : JsProxy(value) {

    val back by proxy("back")
    val forward by proxy("forward")
    val go by proxy("go")
    val length by proxy("length")
    val pushState by proxy("pushState")
    val replaceState by proxy("replaceState")
    val scrollRestoration by proxy("scrollRestoration")
    val state by proxy("state")

    fun back(): JsCall {
        return (back)()
    }

    fun forward(): JsCall {
        return (forward)()
    }

    fun go(delta: JsExpression = JsUndefined): JsCall {
        return (go)(delta)
    }

    fun pushState(
        state: JsExpression,
        unused: JsExpression,
        url: JsExpression = JsUndefined,
    ): JsCall {
        return (pushState)(
            state,
            unused,
            url,
        )
    }

    fun replaceState(
        state: JsExpression,
        unused: JsExpression,
        url: JsExpression = JsUndefined,
    ): JsCall {
        return (replaceState)(
            state,
            unused,
            url,
        )
    }
}
