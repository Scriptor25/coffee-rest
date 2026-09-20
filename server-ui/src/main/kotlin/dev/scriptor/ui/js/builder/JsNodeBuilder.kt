package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*
import dev.scriptor.ui.js.proxy.Console
import dev.scriptor.ui.js.proxy.Document
import dev.scriptor.ui.js.proxy.Window

abstract class JsNodeBuilder<T> : JsExpressionBuilder<T> {

    val nodes = mutableListOf<JsNode>()

    val console = Console(JsSymbol("console"))
    val window = Window(JsSymbol("window"))
    val document = Document(JsSymbol("document"))

    fun <N : JsNode> emit(node: N): N {
        nodes += node
        return node
    }

    fun jsIfElse(
        condition: JsExpression,
        thenBlock: JsNodesBuilder.() -> Unit,
        elseBlock: JsNodesBuilder.() -> Unit,
    ): JsIfElse {
        val thenNodes = JsNodesBuilder().apply(thenBlock).build()
        val elseNodes = JsNodesBuilder().apply(elseBlock).build()

        return JsIfElse(
            condition,
            JsBlock(thenNodes),
            JsBlock(elseNodes),
        )
    }

    fun jsIf(
        condition: JsExpression,
        thenBlock: JsNodesBuilder.() -> Unit,
    ): JsIfElse {
        val thenNodes = JsNodesBuilder().apply(thenBlock).build()

        return JsIfElse(
            condition,
            JsBlock(thenNodes),
            null,
        )
    }

    fun jsReturn(value: JsExpression = JsUndefined): JsReturn {
        return JsReturn(value)
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
