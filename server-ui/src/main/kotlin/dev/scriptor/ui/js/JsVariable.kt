package dev.scriptor.ui.js

enum class JsVariableKind(val value: String) {
    CONST("const"),
    LET("let"),
}

data class JsVariable(
    val kind: JsVariableKind,
    val name: String,
    val initializer: JsExpression?,
) : JsNode {

    override fun toJsString(): String =
        "$kind $name${if (initializer != null) " = $initializer" else ""}"
}
