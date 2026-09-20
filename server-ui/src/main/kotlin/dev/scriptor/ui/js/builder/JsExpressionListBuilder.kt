package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsExpression

class JsExpressionListBuilder : JsExpressionBuilder<List<JsExpression>> {

    private val list = mutableListOf<JsExpression>()

    override fun build(): List<JsExpression> = list

    operator fun plusAssign(element: JsExpression) {
        list += element
    }
}
