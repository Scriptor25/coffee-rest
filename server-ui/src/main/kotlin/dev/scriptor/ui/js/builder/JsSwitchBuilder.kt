package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

data class JsSwitchBuilder(
    val condition: JsExpression,
) {
    val cases = mutableListOf<JsCase>()

    fun build(): JsSwitch {
        return JsSwitch(
            condition,
            cases,
        )
    }

    fun case(value: JsExpression, block: JsNodesBuilder.() -> Unit) {
        val nodes = JsNodesBuilder().apply(block).build()

        cases += JsCase(value, nodes)
    }

    fun case(value: Boolean, block: JsNodesBuilder.() -> Unit) {
        val nodes = JsNodesBuilder().apply(block).build()

        cases += JsCase(JsBoolean(value), nodes)
    }

    fun case(value: Number, block: JsNodesBuilder.() -> Unit) {
        val nodes = JsNodesBuilder().apply(block).build()

        cases += JsCase(JsNumber(value), nodes)
    }

    fun case(value: String, block: JsNodesBuilder.() -> Unit) {
        val nodes = JsNodesBuilder().apply(block).build()

        cases += JsCase(JsString(value), nodes)
    }
}
