package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*
import dev.scriptor.ui.js.proxy.JsConsoleProxy
import dev.scriptor.ui.js.proxy.JsDocumentProxy
import dev.scriptor.ui.js.proxy.JsWindowProxy

abstract class JsBuilder<T> {

    val nodes = mutableListOf<JsNode>()

    val console = JsConsoleProxy(JsSymbol("console"))
    val window = JsWindowProxy(JsSymbol("window"))
    val document = JsDocumentProxy(JsSymbol("document"))

    abstract fun build(): T

    fun <T : JsNode> emit(node: T) {
        nodes += node
    }

    fun call(callee: JsExpression, vararg arguments: JsExpression): JsCall {
        return JsCall(callee, arguments.asList())
    }

    fun emitCall(callee: JsExpression, vararg arguments: JsExpression) {
        emit(call(callee, *arguments))
    }

    fun new(constructor: JsExpression, vararg arguments: JsExpression): JsNew {
        return JsNew(constructor, arguments.asList())
    }

    fun emitNew(constructor: JsExpression, vararg arguments: JsExpression) {
        emit(new(constructor, *arguments))
    }

    fun operator(kind: JsOperatorKind, vararg arguments: JsExpression): JsOperator {
        return JsOperator(kind, arguments.asList())
    }

    fun emitOperator(kind: JsOperatorKind, vararg arguments: JsExpression) {
        emit(operator(kind, *arguments))
    }

    fun function(
        vararg parameters: String,
        name: String? = null,
        async: Boolean = false,
        block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ): JsFunction {
        return JsFunctionBuilder(async, name, parameters.map { JsParameter(it, false) })
            .apply(block)
            .build()
    }

    fun emitFunction(
        vararg parameters: String,
        name: String? = null,
        async: Boolean = false,
        block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ) {
        val function = JsFunctionBuilder(async, name, parameters.map { JsParameter(it, false) })
            .apply(block)
            .build()

        emit(function)
    }

    fun emitConst(name: String, initializer: JsExpression): JsSymbol {
        emit(JsVariable(JsVariableKind.CONST, name, initializer))
        return JsSymbol(name)
    }

    fun emitLet(name: String, initializer: JsExpression? = null): JsSymbol {
        emit(JsVariable(JsVariableKind.LET, name, initializer))
        return JsSymbol(name)
    }

    fun emitReturn(value: JsExpression = JsUndefined) {
        emit(JsReturn(value))
    }

    fun emitThrow(value: JsExpression = JsUndefined) {
        emit(JsThrow(value))
    }

    fun emitIf(
        condition: JsExpression,
        thenBlock: JsBlockBuilder.() -> Unit,
        elseBlock: JsBlockBuilder.() -> Unit = {},
    ) {
        val t = JsBlockBuilder().apply(thenBlock).build()
        val e = JsBlockBuilder().apply(elseBlock).build()

        emit(JsIf(condition, t, e))
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
