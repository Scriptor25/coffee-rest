package dev.scriptor.ui.js

class JsObject(
    val fields: Map<JsExpression, JsExpression>,
) : JsExpression {

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
