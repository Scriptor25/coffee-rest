package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsUndefined

class Clipboard(value: JsExpression) : JsProxy(value) {

    val read by proxy("read")
    val readText by proxy("readText")
    val write by proxy("write")
    val writeText by proxy("writeText")

    fun read(formats: JsExpression = JsUndefined): JsCall {
        return (read)(formats)
    }

    fun readText(): JsCall {
        return (readText)()
    }

    fun write(data: JsExpression): JsCall {
        return (write)(data)
    }

    fun writeText(newClipText: JsExpression): JsCall {
        return (writeText)(newClipText)
    }
}
