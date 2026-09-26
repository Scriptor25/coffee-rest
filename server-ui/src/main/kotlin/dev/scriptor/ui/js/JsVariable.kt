package dev.scriptor.ui.js

enum class JsVariableKind(val value: String) {
    CONST("const"),
    LET("let"),
    USING("using"),
}

data class JsVariable(
    val kind: JsVariableKind,
    val name: String,
    val initializer: JsExpression?,
) : JsNode {

    override fun toJsString(statement: Boolean): String =
        "${kind.value} $name${if (initializer != null) " = ${initializer.toJsString(false)}" else ""}${if (statement) ";" else ""}"
}
