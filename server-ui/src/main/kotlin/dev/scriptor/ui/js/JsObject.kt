package dev.scriptor.ui.js

class JsObject(
    val fields: Map<JsExpression, JsExpression>,
) : JsExpression {

    override fun toJsString(statement: Boolean): String =
        if (statement) ""
        else fields.entries
            .filter { it.value !is JsUndefined }
            .joinToString(",", "{", "}") { (k, v) ->
                val key = when (k) {
                    is JsString ->
                        if (validateKey(k.value)) k.value
                        else k.toJsString(false)

                    else -> "[${k.toJsString(false)}]"
                }

                "$key:${v.toJsString(false)}"
            }
}
