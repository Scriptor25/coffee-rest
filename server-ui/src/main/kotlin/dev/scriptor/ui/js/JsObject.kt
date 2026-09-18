package dev.scriptor.ui.js

class JsObject(
    val fields: Map<JsExpression, JsExpression>,
) : JsExpression {

    constructor(vararg fields: Pair<String, JsExpression>)
            : this(fields.associate { Pair(JsString(it.first), it.second) })

    constructor(block: MutableMap<String, JsExpression>.() -> Unit)
            : this(mutableMapOf<String, JsExpression>().apply(block).mapKeys { JsString(it.key) })

    override fun toJsString(): String =
        fields.entries
            .filter { it.value !is JsUndefined }
            .joinToString(",", "{", "}") { (k, v) ->
                val key = when (k) {
                    is JsString ->
                        if (validateKey(k.value)) k.value
                        else k.toJsString()

                    else -> "[${k.toJsString()}]"
                }

                "$key:${v.toJsString()}"
            }
}
