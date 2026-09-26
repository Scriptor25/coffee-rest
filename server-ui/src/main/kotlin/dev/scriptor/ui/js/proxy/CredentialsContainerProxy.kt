package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class CredentialsContainerProxy(value: JsExpression) : JsProxy(value) {

    val create by proxy("create")
    val get by proxy("get")
    val preventSilentAccess by proxy("preventSilentAccess")
    val store by proxy("store")

    fun create(options: JsExpression = JsUndefined): JsCall {
        return (create)(options)
    }

    fun get1(options: JsExpression = JsUndefined): JsCall {
        return (get)(options)
    }

    fun preventSilentAccess(): JsCall {
        return (preventSilentAccess)()
    }

    fun store(credentials: JsExpression): JsCall {
        return (store)(credentials)
    }
}
