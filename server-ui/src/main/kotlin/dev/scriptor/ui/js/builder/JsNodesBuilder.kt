package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsNode

class JsNodesBuilder : JsBuilder<List<JsNode>>() {

    override fun build(): List<JsNode> {
        return nodes
    }
}
