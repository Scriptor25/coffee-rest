package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsNode

class JsNodesBuilder : JsNodeBuilder<List<JsNode>>() {

    override fun build(): List<JsNode> = nodes
}
