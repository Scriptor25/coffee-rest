package dev.scriptor.ui.js

import kotlin.reflect.KProperty

interface JsExpression : JsNode {

    data class TypedProxy<T : JsExpression>(val get: () -> T) {

        operator fun getValue(self: Any?, property: KProperty<*>): T = get()
    }

    data class Proxy(val get: () -> JsExpression) {

        operator fun getValue(self: Any?, property: KProperty<*>): JsExpression = get()
    }

    fun <T : JsExpression> proxy(name: String, factory: (JsExpression) -> T): TypedProxy<T> {
        return TypedProxy(
            get = { factory(get(name)) },
        )
    }

    fun proxy(name: String): Proxy {
        return Proxy(
            get = { get(name) },
        )
    }

    operator fun get(index: Number): JsMember {
        return get(JsNumber(index))
    }

    operator fun get(name: String): JsMember {
        return get(JsString(name))
    }

    operator fun get(name: JsExpression): JsMember {
        return JsMember(this, name)
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

    infix fun assign(other: Boolean): JsExpression {
        return JsOperator(JsOperatorKind.ASSIGN, listOf(this, JsBoolean(other)))
    }

    infix fun assign(other: Number): JsExpression {
        return JsOperator(JsOperatorKind.ASSIGN, listOf(this, JsNumber(other)))
    }

    infix fun assign(other: String): JsExpression {
        return JsOperator(JsOperatorKind.ASSIGN, listOf(this, JsString(other)))
    }

    infix fun assign(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.ASSIGN, listOf(this, other))
    }

    operator fun plus(other: Number): JsOperator {
        return JsOperator(JsOperatorKind.ADD, listOf(this, JsNumber(other)))
    }

    operator fun Number.plus(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.ADD, listOf(JsNumber(this), other))
    }

    operator fun plus(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.ADD, listOf(this, other))
    }

    operator fun minus(other: Number): JsOperator {
        return JsOperator(JsOperatorKind.SUB, listOf(this, JsNumber(other)))
    }

    operator fun Number.minus(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.SUB, listOf(JsNumber(this), other))
    }

    operator fun minus(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.SUB, listOf(this, other))
    }

    operator fun times(other: Number): JsOperator {
        return JsOperator(JsOperatorKind.MUL, listOf(this, JsNumber(other)))
    }

    operator fun Number.times(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.MUL, listOf(JsNumber(this), other))
    }

    operator fun times(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.MUL, listOf(this, other))
    }

    operator fun div(other: Number): JsOperator {
        return JsOperator(JsOperatorKind.DIV, listOf(this, JsNumber(other)))
    }

    operator fun Number.div(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.DIV, listOf(JsNumber(this), other))
    }

    operator fun div(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.DIV, listOf(this, other))
    }

    operator fun rem(other: Number): JsOperator {
        return JsOperator(JsOperatorKind.REM, listOf(this, JsNumber(other)))
    }

    operator fun Number.rem(other: JsExpression): JsOperator {
        return JsOperator(JsOperatorKind.REM, listOf(JsNumber(this), other))
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

    infix fun exp(other: Number): JsExpression {
        return JsOperator(JsOperatorKind.EXP, listOf(this, JsNumber(other)))
    }

    infix fun Number.exp(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.EXP, listOf(JsNumber(this), other))
    }

    infix fun exp(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.EXP, listOf(this, other))
    }

    infix fun and(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.LOGICAL_AND, listOf(this, other))
    }

    infix fun or(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.LOGICAL_OR, listOf(this, other))
    }

    infix fun eq(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_EQUAL, listOf(this, other))
    }

    infix fun eq(other: Boolean): JsExpression {
        return eq(JsBoolean(other))
    }

    infix fun Boolean.eq(other: JsExpression): JsExpression {
        return JsBoolean(this).eq(other)
    }

    infix fun eq(other: Number): JsExpression {
        return eq(JsNumber(other))
    }

    infix fun Number.eq(other: JsExpression): JsExpression {
        return JsNumber(this).eq(other)
    }

    infix fun eq(other: String): JsExpression {
        return eq(JsString(other))
    }

    infix fun String.eq(other: JsExpression): JsExpression {
        return JsString(this).eq(other)
    }

    infix fun ne(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_NOT_EQUAL, listOf(this, other))
    }

    infix fun ne(other: Boolean): JsExpression {
        return ne(JsBoolean(other))
    }

    infix fun Boolean.ne(other: JsExpression): JsExpression {
        return JsBoolean(this).ne(other)
    }

    infix fun ne(other: Number): JsExpression {
        return ne(JsNumber(other))
    }

    infix fun Number.ne(other: JsExpression): JsExpression {
        return JsNumber(this).ne(other)
    }

    infix fun ne(other: String): JsExpression {
        return ne(JsString(other))
    }

    infix fun String.ne(other: JsExpression): JsExpression {
        return JsString(this).ne(other)
    }

    infix fun seq(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_STRICT_EQUAL, listOf(this, other))
    }

    infix fun seq(other: Boolean): JsExpression {
        return seq(JsBoolean(other))
    }

    infix fun Boolean.seq(other: JsExpression): JsExpression {
        return JsBoolean(this).seq(other)
    }

    infix fun seq(other: Number): JsExpression {
        return seq(JsNumber(other))
    }

    infix fun Number.seq(other: JsExpression): JsExpression {
        return JsNumber(this).seq(other)
    }

    infix fun seq(other: String): JsExpression {
        return seq(JsString(other))
    }

    infix fun String.seq(other: JsExpression): JsExpression {
        return JsString(this).seq(other)
    }

    infix fun sne(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_STRICT_NOT_EQUAL, listOf(this, other))
    }

    infix fun sne(other: Boolean): JsExpression {
        return sne(JsBoolean(other))
    }

    infix fun Boolean.sne(other: JsExpression): JsExpression {
        return JsBoolean(this).sne(other)
    }

    infix fun sne(other: Number): JsExpression {
        return sne(JsNumber(other))
    }

    infix fun Number.sne(other: JsExpression): JsExpression {
        return JsNumber(this).sne(other)
    }

    infix fun sne(other: String): JsExpression {
        return sne(JsString(other))
    }

    infix fun String.sne(other: JsExpression): JsExpression {
        return JsString(this).sne(other)
    }

    infix fun lt(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_LESS_THAN, listOf(this, other))
    }

    infix fun lt(other: Number): JsExpression {
        return lt(JsNumber(other))
    }

    infix fun Number.lt(other: JsExpression): JsExpression {
        return JsNumber(this).lt(other)
    }

    infix fun lte(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_LESS_THAN_OR_EQUAL, listOf(this, other))
    }

    infix fun lte(other: Number): JsExpression {
        return lte(JsNumber(other))
    }

    infix fun Number.lte(other: JsExpression): JsExpression {
        return JsNumber(this).lte(other)
    }

    infix fun gt(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_GREATER_THAN, listOf(this, other))
    }

    infix fun gt(other: Number): JsExpression {
        return gt(JsNumber(other))
    }

    infix fun Number.gt(other: JsExpression): JsExpression {
        return JsNumber(this).gt(other)
    }

    infix fun gte(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.COMPARE_GREATER_THAN_OR_EQUAL, listOf(this, other))
    }

    infix fun gte(other: Number): JsExpression {
        return gte(JsNumber(other))
    }

    infix fun Number.gte(other: JsExpression): JsExpression {
        return JsNumber(this).gte(other)
    }

    fun spread(): JsExpression {
        return JsOperator(JsOperatorKind.SPREAD, listOf(this))
    }

    infix fun instanceof(other: JsExpression): JsExpression {
        return JsOperator(JsOperatorKind.INSTANCEOF, listOf(this, other))
    }
}
