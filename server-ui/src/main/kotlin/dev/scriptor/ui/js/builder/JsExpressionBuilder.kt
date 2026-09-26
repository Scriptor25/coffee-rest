package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

interface JsExpressionBuilder<T> {

    fun build(): T

    fun jsArray(elements: List<JsExpression>): JsArray {
        return JsArray(elements)
    }

    fun jsArray(vararg elements: JsExpression): JsArray {
        return JsArray(elements.asList())
    }

    fun jsArray(block: JsExpressionListBuilder.() -> Unit): JsArray {
        return JsArray(JsExpressionListBuilder().apply(block).build())
    }

    fun jsBoolean(value: Boolean): JsBoolean {
        return JsBoolean(value)
    }

    fun jsCall(
        callee: JsExpression,
        arguments: List<JsExpression>,
    ): JsCall {
        return JsCall(
            callee,
            arguments,
        )
    }

    fun jsCall(
        callee: JsExpression,
        vararg arguments: JsExpression,
    ): JsCall {
        return JsCall(
            callee,
            arguments.asList(),
        )
    }

    fun jsCall(
        callee: JsExpression,
        block: JsExpressionListBuilder.() -> Unit,
    ): JsCall {
        return JsCall(
            callee,
            JsExpressionListBuilder().apply(block).build(),
        )
    }

    fun jsFormat(
        strings: List<String>,
        values: List<JsExpression>,
    ): JsFormat {
        return JsFormat(
            strings,
            values,
        )
    }

    fun jsFormat(
        vararg strings: String,
        values: List<JsExpression>,
    ): JsFormat {
        return JsFormat(
            strings.asList(),
            values,
        )
    }

    fun jsFunction(
        async: Boolean = false,
        parameters: List<JsParameter> = emptyList(),
        nodes: List<JsNode> = emptyList(),
    ): JsFunction {
        return JsFunction(
            async,
            null,
            parameters,
            JsBlock(nodes),
        )
    }

    fun jsFunction(
        vararg parameters: String,
        async: Boolean = false,
        block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ): JsFunction {
        return JsFunctionBuilder(
            async,
            null,
            parameters.map { JsParameter(it, false) },
        ).apply(block).build()
    }

    fun jsMember(
        value: JsExpression,
        name: JsExpression,
    ): JsMember {
        return JsMember(
            value,
            name,
        )
    }

    fun jsMember(
        value: JsExpression,
        name: String,
    ): JsMember {
        return JsMember(
            value,
            JsString(name),
        )
    }

    fun jsNew(
        constructor: JsExpression,
        arguments: List<JsExpression>,
    ): JsNew {
        return JsNew(
            constructor,
            arguments,
        )
    }

    fun jsNew(
        constructor: JsExpression,
        vararg arguments: JsExpression,
    ): JsNew {
        return JsNew(
            constructor,
            arguments.asList(),
        )
    }

    fun jsNew(
        constructor: JsExpression,
        block: JsExpressionListBuilder.() -> Unit,
    ): JsNew {
        return JsNew(
            constructor,
            JsExpressionListBuilder().apply(block).build(),
        )
    }

    fun jsNumber(value: Number): JsNumber {
        return JsNumber(value)
    }

    fun jsObject(fields: Map<JsExpression, JsExpression>): JsObject {
        return JsObject(fields)
    }

    fun jsObject(vararg fields: Pair<JsExpression, JsExpression>): JsObject {
        return JsObject(fields.toMap())
    }

    fun jsObject(block: JsExpressionMapBuilder.() -> Unit): JsObject {
        return JsObject(JsExpressionMapBuilder().apply(block).build())
    }

    fun jsString(value: String): JsString {
        return JsString(value)
    }

    fun jsTernary(
        condition: JsExpression,
        thenValue: JsExpression,
        elseValue: JsExpression,
    ): JsExpression {
        return JsTernary(
            condition,
            thenValue,
            elseValue,
        )
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
