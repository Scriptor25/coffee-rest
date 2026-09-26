package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString

class DocumentProxy(value: JsExpression) : EventTargetProxy(value) {

    val body by proxy("body", ::ElementProxy)
    val documentElement by proxy("documentElement", ::ElementProxy)
    val getElementById by proxy("getElementById")
    val querySelector by proxy("querySelector")
    val querySelectorAll by proxy("querySelectorAll")

    fun getElementById(id: String): ElementProxy {
        return ElementProxy((getElementById)(JsString(id)))
    }

    fun getElementById(id: JsExpression): ElementProxy {
        return ElementProxy((getElementById)(id))
    }

    fun querySelector(selectors: String): ElementProxy {
        return ElementProxy((querySelector)(JsString(selectors)))
    }

    fun querySelector(selectors: JsExpression): ElementProxy {
        return ElementProxy((querySelector)(selectors))
    }

    fun querySelectorAll(selectors: String): JsCall {
        return (querySelectorAll)(JsString(selectors))
    }

    fun querySelectorAll(selectors: JsExpression): JsCall {
        return (querySelectorAll)(selectors)
    }
}
