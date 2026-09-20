package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class ContactsManager(value: JsExpression) : JsProxy(value) {

    val getProperties by proxy("getProperties")
    val select by proxy("select")

    fun getProperties(): JsCall {
        return (getProperties)()
    }

    fun select(properties: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return (select)(properties, options)
    }
}
