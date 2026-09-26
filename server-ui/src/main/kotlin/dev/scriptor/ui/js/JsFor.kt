package dev.scriptor.ui.js

data class JsFor(
    val prefix: JsNode?,
    val condition: JsExpression?,
    val suffix: JsNode?,
    val node: JsNode,
) : JsNode {

    override fun toJsString(): String =
        "for (${
            prefix?.toJsString() ?: ""
        }; ${
            condition?.toJsString() ?: ""
        }; ${
            suffix?.toJsString() ?: ""
        }) ${node.toJsString()}"
}
