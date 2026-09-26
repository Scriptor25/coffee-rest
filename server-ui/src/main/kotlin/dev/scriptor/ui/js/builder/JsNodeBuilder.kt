package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*
import dev.scriptor.ui.js.proxy.ConsoleProxy
import dev.scriptor.ui.js.proxy.DocumentProxy
import dev.scriptor.ui.js.proxy.MathProxy
import dev.scriptor.ui.js.proxy.WindowProxy

abstract class JsNodeBuilder<T> : JsExpressionBuilder<T> {

    val nodes = mutableListOf<JsNode>()

    val console = ConsoleProxy(JsSymbol("console"))
    val window = WindowProxy(JsSymbol("window"))
    val document = DocumentProxy(JsSymbol("document"))

    val math = MathProxy(JsSymbol("Math"))

    fun emit(node: JsNode) {
        nodes += node
    }

    fun jsFunction(
        async: Boolean = false,
        name: String,
        parameters: List<JsParameter> = emptyList(),
        nodes: List<JsNode> = emptyList(),
    ): JsSymbol {
        emit(
            JsFunction(
                async,
                name,
                parameters,
                JsBlock(nodes),
            ),
        )

        return JsSymbol(name)
    }

    fun jsFunction(
        vararg parameters: String,
        async: Boolean = false,
        name: String,
        block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ): JsSymbol {
        emit(
            JsFunctionBuilder(
                async,
                name,
                parameters.map { JsParameter(it, false) },
            ).apply(block).build(),
        )

        return JsSymbol(name)
    }

    fun jsFunction(
        vararg parameters: String,
        async: Boolean = false,
        name: String,
        block: JsFunctionBuilder.(JsSymbol, Array<JsSymbol>) -> Unit,
    ): JsSymbol {
        emit(
            JsFunctionBuilder(
                async,
                name,
                parameters.map { JsParameter(it, false) },
            ).apply(block).build(),
        )

        return JsSymbol(name)
    }

    fun jsIfElse(
        condition: JsExpression,
        thenBlock: JsNodesBuilder.() -> Unit,
        elseBlock: JsNodesBuilder.() -> Unit,
    ) {
        val thenNodes = JsNodesBuilder().apply(thenBlock).build()
        val elseNodes = JsNodesBuilder().apply(elseBlock).build()

        emit(
            JsIfElse(
                condition,
                JsBlock(thenNodes),
                JsBlock(elseNodes),
            )
        )
    }

    fun jsIf(
        condition: JsExpression,
        thenBlock: JsNodesBuilder.() -> Unit,
    ) {
        val thenNodes = JsNodesBuilder().apply(thenBlock).build()

        emit(
            JsIfElse(
                condition,
                JsBlock(thenNodes),
                null,
            )
        )
    }

    fun jsFor(
        prefix: JsNode? = null,
        condition: JsExpression? = null,
        suffix: JsNode? = null,
        node: JsNode,
    ) {
        emit(
            JsFor(
                prefix,
                condition,
                suffix,
                node,
            )
        )
    }

    fun jsFor(
        prefix: JsNode? = null,
        condition: JsExpression? = null,
        suffix: JsNode? = null,
        block: JsNodesBuilder.() -> Unit = {},
    ) {
        val nodes = JsNodesBuilder().apply(block).build()
        val node = if (nodes.size == 1) nodes[0] else JsBlock(nodes)

        emit(
            JsFor(
                prefix,
                condition,
                suffix,
                node,
            )
        )
    }

    fun jsForEach(
        kind: JsForEachKind,
        iteratorKind: JsVariableKind,
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        emit(
            JsForEach(
                kind,
                iteratorKind,
                name,
                value,
                node,
            )
        )
    }

    fun jsForEach(
        kind: JsForEachKind,
        iteratorKind: JsVariableKind,
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        emit(
            JsForEachBuilder(
                kind,
                iteratorKind,
                name,
                value,
            ).apply(block).build(),
        )
    }

    fun jsForEachConstIn(
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        jsForEach(
            JsForEachKind.IN,
            JsVariableKind.CONST,
            name,
            value,
            node,
        )
    }

    fun jsForEachConstIn(
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        jsForEach(
            JsForEachKind.IN,
            JsVariableKind.CONST,
            name,
            value,
            block,
        )
    }

    fun jsForEachLetIn(
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        jsForEach(
            JsForEachKind.IN,
            JsVariableKind.LET,
            name,
            value,
            node,
        )
    }

    fun jsForEachLetIn(
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        jsForEach(
            JsForEachKind.IN,
            JsVariableKind.LET,
            name,
            value,
            block,
        )
    }

    fun jsForEachUsingIn(
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        jsForEach(
            JsForEachKind.IN,
            JsVariableKind.USING,
            name,
            value,
            node,
        )
    }

    fun jsForEachUsingIn(
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        jsForEach(
            JsForEachKind.IN,
            JsVariableKind.USING,
            name,
            value,
            block,
        )
    }

    fun jsForEachConstOf(
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        jsForEach(
            JsForEachKind.OF,
            JsVariableKind.CONST,
            name,
            value,
            node,
        )
    }

    fun jsForEachConstOf(
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        jsForEach(
            JsForEachKind.OF,
            JsVariableKind.CONST,
            name,
            value,
            block,
        )
    }

    fun jsForEachLetOf(
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        jsForEach(
            JsForEachKind.OF,
            JsVariableKind.LET,
            name,
            value,
            node,
        )
    }

    fun jsForEachLetOf(
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        jsForEach(
            JsForEachKind.OF,
            JsVariableKind.LET,
            name,
            value,
            block,
        )
    }

    fun jsForEachUsingOf(
        name: String,
        value: JsExpression,
        node: JsNode,
    ) {
        jsForEach(
            JsForEachKind.OF,
            JsVariableKind.USING,
            name,
            value,
            node,
        )
    }

    fun jsForEachUsingOf(
        name: String,
        value: JsExpression,
        block: JsForEachBuilder.(JsSymbol) -> Unit = {},
    ) {
        jsForEach(
            JsForEachKind.OF,
            JsVariableKind.USING,
            name,
            value,
            block,
        )
    }

    fun jsReturn(value: Boolean) {
        jsReturn(JsBoolean(value))
    }

    fun jsReturn(value: Number) {
        jsReturn(JsNumber(value))
    }

    fun jsReturn(value: String) {
        jsReturn(JsString(value))
    }

    fun jsReturn(value: JsExpression = JsUndefined) {
        emit(JsReturn(value))
    }

    fun jsThrow(value: JsExpression = JsUndefined) {
        emit(JsThrow(value))
    }

    fun jsContinue() {
        emit(JsContinue)
    }

    fun jsBreak() {
        emit(JsBreak)
    }

    fun jsVariable(
        kind: JsVariableKind,
        name: String,
        initializer: JsExpression? = null,
    ): JsSymbol {
        emit(
            JsVariable(
                kind,
                name,
                initializer,
            )
        )

        return JsSymbol(name)
    }

    fun jsVariable(
        kind: JsVariableKind,
        name: String,
        initializer: Boolean,
    ): JsSymbol {
        emit(
            JsVariable(
                kind,
                name,
                JsBoolean(initializer),
            )
        )

        return JsSymbol(name)
    }

    fun jsVariable(
        kind: JsVariableKind,
        name: String,
        initializer: Number,
    ): JsSymbol {
        emit(
            JsVariable(
                kind,
                name,
                JsNumber(initializer),
            )
        )

        return JsSymbol(name)
    }

    fun jsVariable(
        kind: JsVariableKind,
        name: String,
        initializer: String,
    ): JsSymbol {
        emit(
            JsVariable(
                kind,
                name,
                JsString(initializer),
            )
        )

        return JsSymbol(name)
    }

    fun jsConst(
        name: String,
        initializer: JsExpression,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.CONST,
            name,
            initializer,
        )
    }

    fun jsConst(
        name: String,
        initializer: Boolean,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.CONST,
            name,
            initializer,
        )
    }

    fun jsConst(
        name: String,
        initializer: Number,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.CONST,
            name,
            initializer,
        )
    }

    fun jsConst(
        name: String,
        initializer: String,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.CONST,
            name,
            initializer,
        )
    }

    fun jsLet(
        name: String,
        initializer: JsExpression? = null,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.LET,
            name,
            initializer,
        )
    }

    fun jsLet(
        name: String,
        initializer: Boolean,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.LET,
            name,
            initializer,
        )
    }

    fun jsLet(
        name: String,
        initializer: Number,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.LET,
            name,
            initializer,
        )
    }

    fun jsLet(
        name: String,
        initializer: String,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.LET,
            name,
            initializer,
        )
    }

    fun jsUsing(
        name: String,
        initializer: JsExpression,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.USING,
            name,
            initializer,
        )
    }

    fun jsUsing(
        name: String,
        initializer: Boolean,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.USING,
            name,
            initializer,
        )
    }

    fun jsUsing(
        name: String,
        initializer: Number,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.USING,
            name,
            initializer,
        )
    }

    fun jsUsing(
        name: String,
        initializer: String,
    ): JsSymbol {
        return jsVariable(
            JsVariableKind.USING,
            name,
            initializer,
        )
    }

    fun jsWhile(
        condition: JsExpression,
        block: JsNodesBuilder.() -> Unit = {},
    ) {
        val nodes = JsNodesBuilder().apply(block).build()
        val node = if (nodes.size == 1) nodes[0] else JsBlock(nodes)

        emit(
            JsWhile(
                condition,
                node,
            )
        )
    }

    fun jsDoWhile(
        condition: JsExpression,
        block: JsNodesBuilder.() -> Unit = {},
    ) {
        val nodes = JsNodesBuilder().apply(block).build()
        val node = if (nodes.size == 1) nodes[0] else JsBlock(nodes)

        emit(
            JsDoWhile(
                condition,
                node,
            )
        )
    }

    fun jsSwitch(
        condition: JsExpression,
        cases: List<JsCase>,
    ) {
        emit(
            JsSwitch(
                condition,
                cases,
            )
        )
    }

    fun jsSwitch(
        condition: JsExpression,
        block: JsSwitchBuilder.() -> Unit = {},
    ) {
        val node = JsSwitchBuilder(condition).apply(block).build()

        emit(node)
    }
}
