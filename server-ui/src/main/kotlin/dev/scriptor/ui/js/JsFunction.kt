package dev.scriptor.ui.js

data class JsFunction(
    val async: Boolean,
    val name: String?,
    val parameters: List<JsParameter>,
    val node: JsBlock,
) : JsExpression {

    override fun toJsString(): String =
        "${if (async) "async " else ""}function${if (name != null) " $name" else ""}${
            parameters.joinToString(
                ",",
                "(",
                ")",
                transform = JsParameter::toJsString,
            )
        }${node.toJsString()}"
}
