package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class Navigation(value: JsExpression) : JsProxy(value) {

    val activation by proxy("activation")
    val back by proxy("back")
    val canGoBack by proxy("canGoBack")
    val canGoForward by proxy("canGoForward")
    val currentEntry by proxy("currentEntry")
    val entries by proxy("entries")
    val forward by proxy("forward")
    val navigate by proxy("navigate")
    val reload by proxy("reload")
    val transition by proxy("transition")
    val traverseTo by proxy("traverseTo")
    val updateCurrentEntry by proxy("updateCurrentEntry")

    fun back(options: JsExpression = JsUndefined): JsCall {
        return (back)(options)
    }

    fun entries(): JsCall {
        return (entries)()
    }

    fun forward(options: JsExpression = JsUndefined): JsCall {
        return (forward)(options)
    }

    fun navigate(url: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return (navigate)(url, options)
    }

    fun reload(options: JsExpression = JsUndefined): JsCall {
        return (reload)(options)
    }

    fun traverseTo(key: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return (traverseTo)(key, options)
    }

    fun updateCurrentEntry(options: JsExpression): JsCall {
        return (updateCurrentEntry)(options)
    }
}
