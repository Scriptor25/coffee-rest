package dev.scriptor.ui.js

enum class JsForEachKind(val value: String) {
    IN("in"),
    OF("of"),
}

data class JsForEach(
    val kind: JsForEachKind,
    val name: String,
    val value: JsExpression,
    val body: JsNode,
) : JsNode {

    override fun toJsString(): String {
        require(validateKey(name))

        return "for ($name ${kind.value} ${value.toJsString()}) ${body.toJsString()}"
    }
}
