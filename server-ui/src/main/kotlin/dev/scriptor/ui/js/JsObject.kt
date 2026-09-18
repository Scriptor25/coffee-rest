package dev.scriptor.ui.js

class JsObject(
    val fields: Map<String, JsExpression>,
) : JsExpression {

    constructor(vararg fields: Pair<String, JsExpression>) : this(mapOf(*fields))

    // TODO: sanitize keys, use `[...]` if not sane
    override fun toJsString(): String =
        fields.entries
            .filter { it.value !is JsUndefined }
            .joinToString(",", "{", "}") { (k, v) -> "$k: ${v.toJsString()}" }
}
