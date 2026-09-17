package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

abstract class JsBuilder<T> {

    val nodes = mutableListOf<JsNode>()

    val console = JsConsoleBuilder(this)

    abstract fun build(): T

    fun const(name: String, initializer: JsExpression): JsVariable {
        val node = JsVariable(JsVariableKind.CONST, name, initializer)
        nodes += node
        return node
    }

    fun let(name: String, initializer: JsExpression? = null): JsVariable {
        val node = JsVariable(JsVariableKind.LET, name, initializer)
        nodes += node
        return node
    }

    fun call(callee: JsExpression, vararg arguments: JsExpression): JsCall {
        val node = JsCall(callee, arguments.asList())
        nodes += node
        return node
    }
}
