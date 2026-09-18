package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

abstract class JsBuilder<T> {

    val nodes = mutableListOf<JsNode>()

    val console = JsConsoleProxy
    val window = JsWindowProxy
    val document = JsDocumentProxy

    abstract fun build(): T

    fun <T : JsNode> emit(node: T): T {
        nodes += node
        return node
    }

    fun call(callee: JsExpression, vararg arguments: JsExpression): JsCall {
        return emit(JsCall(callee, arguments.asList()))
    }

    fun new(constructor: JsExpression, vararg arguments: JsExpression): JsNew {
        return emit(JsNew(constructor, arguments.asList()))
    }

    fun operator(kind: JsOperatorKind, vararg arguments: JsExpression): JsOperator {
        return emit(JsOperator(kind, arguments.asList()))
    }

    fun const(name: String, initializer: JsExpression): JsSymbol {
        emit(JsVariable(JsVariableKind.CONST, name, initializer))
        return JsSymbol(name)
    }

    fun let(name: String, initializer: JsExpression? = null): JsSymbol {
        emit(JsVariable(JsVariableKind.LET, name, initializer))
        return JsSymbol(name)
    }

    fun eval(script: String): JsCall {
        return eval(JsString(script))
    }

    fun eval(script: JsExpression): JsCall {
        return JsSymbol("eval")(script)
    }

    fun isFinite(value: JsExpression): JsCall {
        return JsSymbol("isFinite")(value)
    }

    fun isNaN(value: JsExpression): JsCall {
        return JsSymbol("isNaN")(value)
    }

    fun parseFloat(string: String): JsCall {
        return parseFloat(JsString(string))
    }

    fun parseFloat(string: JsExpression): JsCall {
        return JsSymbol("parseFloat")(string)
    }

    fun parseInt(string: String, radix: Int? = null): JsCall {
        return parseInt(
            JsString(string),
            radix?.let { JsNumber(it) } ?: JsUndefined,
        )
    }

    fun parseInt(string: JsExpression, radix: JsExpression = JsUndefined): JsCall {
        return JsSymbol("parseInt")(string, radix)
    }

    fun decodeURI(uri: JsExpression): JsCall {
        return JsSymbol("decodeURI")(uri)
    }

    fun decodeURIComponent(component: JsExpression): JsCall {
        return JsSymbol("decodeURIComponent")(component)
    }

    fun encodeURI(uri: JsExpression): JsCall {
        return JsSymbol("encodeURI")(uri)
    }

    fun encodeURIComponent(component: JsExpression): JsCall {
        return JsSymbol("encodeURIComponent")(component)
    }

    fun escape(str: JsExpression): JsCall {
        return JsSymbol("escape")(str)
    }

    fun unescape(str: JsExpression): JsCall {
        return JsSymbol("unescape")(str)
    }
}
