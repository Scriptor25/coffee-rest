package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString
import dev.scriptor.ui.js.JsUndefined

class WindowProxy(value: JsExpression) : EventTargetProxy(value) {

    val fetch by proxy("fetch")
    val history by proxy("history", ::HistoryProxy)
    val location by proxy("location", ::LocationProxy)
    val navigation by proxy("navigation", ::NavigationProxy)
    val navigator by proxy("navigator", ::NavigatorProxy)
    val open by proxy("open")
    val requestAnimationFrame by proxy("requestAnimationFrame")
    val scrollBy by proxy("scrollBy")
    val sessionStorage by proxy("sessionStorage", ::StorageProxy)

    fun fetch(resource: String, options: JsExpression = JsUndefined): JsPromiseProxy {
        return fetch(JsString(resource), options)
    }

    fun fetch(resource: JsExpression, options: JsExpression = JsUndefined): JsPromiseProxy {
        return JsPromiseProxy((fetch)(resource, options))
    }

    fun open(url: JsExpression): JsCall {
        return (open)(url)
    }

    fun requestAnimationFrame(callback: JsExpression): JsCall {
        return (requestAnimationFrame)(callback)
    }

    fun scrollBy(options: JsExpression): JsCall {
        return (scrollBy)(options)
    }
}
