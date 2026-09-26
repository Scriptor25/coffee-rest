package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsParameter
import dev.scriptor.ui.js.JsSymbol
import dev.scriptor.ui.js.JsUndefined
import dev.scriptor.ui.js.builder.JsFunctionBuilder

class JsPromiseProxy(value: JsExpression) : JsProxy(value) {

    val catch by proxy("catch")
    val finally by proxy("finally")
    val then by proxy("then")

    fun catch(onRejected: JsExpression): JsPromiseProxy {
        return JsPromiseProxy((catch)(onRejected))
    }

    fun catch(async: Boolean = false, block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit): JsPromiseProxy {
        val onRejected = JsFunctionBuilder(
            async,
            null,
            listOf(JsParameter("reason", false)),
        ).apply(block).build()

        return catch(onRejected)
    }

    fun finally(onFinally: JsExpression): JsPromiseProxy {
        return JsPromiseProxy((finally)(onFinally))
    }

    fun finally(async: Boolean = false, block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit): JsPromiseProxy {
        val onFinally = JsFunctionBuilder(
            async,
            null,
            listOf(),
        ).apply(block).build()

        return finally(onFinally)
    }

    fun then(onFulfilled: JsExpression, onRejected: JsExpression = JsUndefined): JsPromiseProxy {
        return JsPromiseProxy((then)(onFulfilled, onRejected))
    }

    fun then(async: Boolean = false, block: JsFunctionBuilder.(Array<JsSymbol>) -> Unit): JsPromiseProxy {
        val onFulfilled = JsFunctionBuilder(
            async,
            null,
            listOf(JsParameter("value", false)),
        ).apply(block).build()

        return then(onFulfilled)
    }

    fun then(
        async: Boolean = false,
        fulfilledBlock: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
        rejectedBlock: JsFunctionBuilder.(Array<JsSymbol>) -> Unit,
    ): JsPromiseProxy {
        val onFulfilled = JsFunctionBuilder(
            async,
            null,
            listOf(JsParameter("value", false)),
        ).apply(fulfilledBlock).build()

        val onRejected = JsFunctionBuilder(
            async,
            null,
            listOf(JsParameter("value", false)),
        ).apply(rejectedBlock).build()

        return then(onFulfilled, onRejected)
    }
}
