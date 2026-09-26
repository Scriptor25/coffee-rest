package dev.scriptor.ui.js

data class JsFor(
    val prefix: JsNode?,
    val condition: JsExpression?,
    val suffix: JsNode?,
    val node: JsNode,
) : JsNode {

    override fun toJsString(statement: Boolean): String =
        "for (${
            prefix?.toJsString(false) ?: ""
        }; ${
            condition?.toJsString(false) ?: ""
        }; ${
            suffix?.toJsString(false) ?: ""
        }) ${node.toJsString(true)}"
}
