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

fun jsBlock(nodes: List<JsNode>): JsBlock {
    return JsBlock(nodes)
}

fun jsBlock(vararg nodes: JsNode): JsBlock {
    return JsBlock(nodes.asList())
}

fun jsBlock(block: MutableList<JsNode>.() -> Unit): JsBlock {
    return JsBlock(mutableListOf<JsNode>().apply(block))
}
