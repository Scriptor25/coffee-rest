package dev.scriptor.ui.js

interface JsExpression : JsNode {

    operator fun get(name: String): JsMember {
        return get(JsString(name))
    }

    operator fun get(name: JsExpression): JsMember {
        return JsMember(this, name)
    }

    operator fun set(name: String, value: JsExpression) {
        set(name, JsString(name))
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
}
