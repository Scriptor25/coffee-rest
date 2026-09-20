package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsNumber
import dev.scriptor.ui.js.JsString

class JsExpressionMapBuilder : JsExpressionBuilder<Map<JsExpression, JsExpression>> {

    private val map = mutableMapOf<JsExpression, JsExpression>()

    override fun build(): Map<JsExpression, JsExpression> = map

    operator fun set(key: JsExpression, value: JsExpression) {
        map[key] = value
    }

    operator fun set(key: Number, value: JsExpression) {
        map[JsNumber(key)] = value
    }

    operator fun set(key: String, value: JsExpression) {
        map[JsString(key)] = value
    }
}
