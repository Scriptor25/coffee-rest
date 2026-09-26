package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsNumber
import dev.scriptor.ui.js.JsString

class StorageProxy(value: JsExpression) : JsProxy(value) {

    val clear by proxy("clear")
    val getItem by proxy("getItem")
    val key by proxy("key")
    val length by proxy("length")
    val removeItem by proxy("removeItem")
    val setItem by proxy("setItem")

    fun clear(): JsCall {
        return (clear)()
    }

    fun getItem(keyName: String): JsCall {
        return (getItem)(JsString(keyName))
    }

    fun getItem(keyName: JsExpression): JsCall {
        return (getItem)(keyName)
    }

    fun key(index: Number): JsCall {
        return (key)(JsNumber(index))
    }

    fun key(index: JsExpression): JsCall {
        return (key)(index)
    }

    fun removeItem(keyName: String): JsCall {
        return (removeItem)(JsString(keyName))
    }

    fun removeItem(keyName: JsExpression): JsCall {
        return (removeItem)(keyName)
    }

    fun setItem(keyName: String, keyValue: JsExpression): JsCall {
        return (setItem)(JsString(keyName), keyValue)
    }

    fun setItem(keyName: JsExpression, keyValue: JsExpression): JsCall {
        return (setItem)(keyName, keyValue)
    }
}
