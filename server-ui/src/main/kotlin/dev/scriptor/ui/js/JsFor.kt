package dev.scriptor.ui.js

import dev.scriptor.ui.js.builder.JsNodesBuilder

data class JsFor(
    val prefix: JsNode?,
    val condition: JsExpression?,
    val suffix: JsNode?,
    val body: JsNode,
) : JsNode {

    override fun toJsString(): String =
        "for (${
            prefix?.toJsString() ?: ""
        }; ${
            condition?.toJsString() ?: ""
        }; ${
            suffix?.toJsString() ?: ""
        }) ${body.toJsString()}"
}

fun jsFor(
    prefix: JsNode? = null,
    condition: JsExpression? = null,
    suffix: JsNode? = null,
    body: JsNode,
): JsFor {
    return JsFor(prefix, condition, suffix, body)
}

fun jsFor(
    prefix: JsNode? = null,
    condition: JsExpression? = null,
    suffix: JsNode? = null,
    block: JsNodesBuilder.() -> Unit,
): JsFor {
    val nodes = JsNodesBuilder().apply(block).build()
    val body = if (nodes.size == 1) nodes[0] else JsBlock(nodes)
    return JsFor(prefix, condition, suffix, body)
}
