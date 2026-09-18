package dev.scriptor.ui.js

import dev.scriptor.ui.js.builder.JsBuilder

interface JsNode {

    fun toJsString(): String

    context(builder: JsBuilder<*>)
    fun emit() {
        builder.emit(this)
    }

    context(builder: JsBuilder<*>)
    operator fun unaryPlus() = emit()
}
