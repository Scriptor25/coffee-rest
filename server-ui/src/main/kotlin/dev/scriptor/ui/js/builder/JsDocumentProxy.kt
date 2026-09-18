package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString
import dev.scriptor.ui.js.JsSymbol

data object JsDocumentProxy : JsSymbol("document") {

    fun querySelector(selectors: String): JsCall {
        return querySelector(JsString(selectors))
    }

    fun querySelector(selectors: JsExpression): JsCall {
        return this["querySelector"](selectors)
    }
}
