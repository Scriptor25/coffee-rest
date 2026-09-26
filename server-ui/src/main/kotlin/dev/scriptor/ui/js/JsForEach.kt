package dev.scriptor.ui.js

enum class JsForEachKind(val value: String) {
    IN("in"),
    OF("of"),
}

data class JsForEach(
    val kind: JsForEachKind,
    val iteratorKind: JsVariableKind,
    val name: String,
    val value: JsExpression,
    val node: JsNode,
) : JsNode {

    override fun toJsString(): String {
        require(validateKey(name))

        return "for (${iteratorKind.value} $name ${kind.value} ${value.toJsString()}) ${node.toJsString()}"
    }
}
