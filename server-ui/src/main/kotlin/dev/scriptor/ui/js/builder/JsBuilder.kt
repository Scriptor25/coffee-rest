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

    fun eval(script: String): JsCall {
        val callee = JsSymbol("eval")
        return call(callee, JsString(script))
    }

    fun eval(script: JsExpression): JsCall {
        val callee = JsSymbol("eval")
        return call(callee, script)
    }

    fun isFinite(value: JsExpression): JsCall {
        val callee = JsSymbol("isFinite")
        return call(callee, value)
    }

    fun isNaN(value: JsExpression): JsCall {
        val callee = JsSymbol("isNaN")
        return call(callee, value)
    }

    fun parseFloat(string: String): JsCall {
        val callee = JsSymbol("parseFloat")
        return call(callee, JsString(string))
    }

    fun parseFloat(string: JsExpression): JsCall {
        val callee = JsSymbol("parseFloat")
        return call(callee, string)
    }

    fun parseInt(string: String, radix: Int? = null): JsCall {
        val callee = JsSymbol("parseInt")
        return call(callee, JsString(string), radix?.let { JsNumber(it) } ?: JsUndefined)
    }

    fun parseInt(string: JsExpression, radix: JsExpression = JsUndefined): JsCall {
        val callee = JsSymbol("parseInt")
        return call(callee, string, radix)
    }

    fun decodeURI(uri: JsExpression): JsCall {
        val callee = JsSymbol("decodeURI")
        return call(callee, uri)
    }

    fun decodeURIComponent(component: JsExpression): JsCall {
        val callee = JsSymbol("decodeURIComponent")
        return call(callee, component)
    }

    fun encodeURI(uri: JsExpression): JsCall {
        val callee = JsSymbol("encodeURI")
        return call(callee, uri)
    }

    fun encodeURIComponent(component: JsExpression): JsCall {
        val callee = JsSymbol("encodeURIComponent")
        return call(callee, component)
    }

    fun escape(str: JsExpression): JsCall {
        val callee = JsSymbol("escape")
        return call(callee, str)
    }

    fun unescape(str: JsExpression): JsCall {
        val callee = JsSymbol("unescape")
        return call(callee, str)
    }
}
