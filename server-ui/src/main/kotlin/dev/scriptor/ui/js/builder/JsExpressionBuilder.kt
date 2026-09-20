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

    fun jsFunction(
        async: Boolean = false,
        name: String? = null,
        parameters: List<JsParameter> = emptyList(),
        nodes: List<JsNode> = emptyList(),
    ): JsFunction {
        return JsFunction(
            async,
            name,
            parameters,
            nodes,
        )
    }

    fun jsFunction(
        vararg parameters: String,
        async: Boolean = false,
        name: String? = null,
        block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ): JsFunction {
        return JsFunctionBuilder(
            async,
            name,
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
}
