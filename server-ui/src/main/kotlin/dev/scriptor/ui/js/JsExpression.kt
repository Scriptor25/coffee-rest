package dev.scriptor.ui.js

import kotlin.reflect.KProperty

interface JsExpression : JsNode {

    data class TypedProxy<T : JsExpression>(val get: () -> T, val set: (T) -> Unit) {

        operator fun getValue(self: Any?, property: KProperty<*>): T = get()

        operator fun setValue(self: Any?, property: KProperty<*>, value: T) = set(value)
    }

    data class Proxy(val get: () -> JsExpression, val set: (JsExpression) -> Unit) {

        operator fun getValue(self: Any?, property: KProperty<*>): JsExpression = get()

        operator fun setValue(self: Any?, property: KProperty<*>, value: JsExpression) = set(value)
    }

    fun <T : JsExpression> proxy(name: String, factory: (JsExpression) -> T): TypedProxy<T> {
        return TypedProxy(
            get = { factory(get(name)) },
            set = { set(name, it) }
        )
    }

    fun proxy(name: String): Proxy {
        return Proxy(
            get = { get(name) },
            set = { set(name, it) }
        )
    }

    operator fun get(name: String): JsMember {
        return get(JsString(name))
    }

    operator fun get(name: JsExpression): JsMember {
        return JsMember(this, name)
    }

    operator fun set(name: String, value: JsExpression) {
        set(JsString(name), value)
    }

    operator fun set(name: JsExpression, value: JsExpression) {
        JsOperator(
            JsOperatorKind.ASSIGN,
            listOf(
                JsMember(this, name),
                value,
            ),
        )
    }

    operator fun invoke(vararg arguments: JsExpression): JsCall {
        return JsCall(
            this,
            arguments.asList(),
        )
    }

    fun new(vararg arguments: JsExpression): JsNew {
        return JsNew(
            this,
            arguments.asList(),
        )
    }

    operator fun plus(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.ADD, listOf(this, other))
    }

    operator fun minus(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.SUB, listOf(this, other))
    }

    operator fun times(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.MUL, listOf(this, other))
    }

    operator fun div(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.DIV, listOf(this, other))
    }

    operator fun rem(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.REM, listOf(this, other))
    }

    operator fun not(): JsOperator {
        return JsOperator(JsOperatorKind.LOGICAL_NOT, listOf(this))
    }

    operator fun unaryPlus(): JsOperator {
        return JsOperator(JsOperatorKind.POS, listOf(this))
    }

    operator fun unaryMinus(): JsOperator {
        return JsOperator(JsOperatorKind.NEG, listOf(this))
    }

    operator fun inc(): JsExpression {
        return JsOperator(JsOperatorKind.GET_THEN_INC, listOf(this))
    }

    operator fun dec(): JsExpression {
        return JsOperator(JsOperatorKind.GET_THEN_DEC, listOf(this))
    }
}
