package dev.scriptor.ui.js

data class JsBlock(
    val nodes: List<JsNode>,
) : JsNode {

    override fun toJsString(): String =
        nodes.joinToString(
            ";",
            "{",
            "}",
            transform = JsNode::toJsString,
        )
}
