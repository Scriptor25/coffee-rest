package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsBlock
import dev.scriptor.ui.js.JsNode

class JsBlockBuilder : JsBuilder<JsBlock> {

    override val nodes = mutableListOf<JsNode>()

    override fun build(): JsBlock {
        return JsBlock(nodes)
    }
}