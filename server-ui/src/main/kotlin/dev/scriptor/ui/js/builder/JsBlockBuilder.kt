package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsBlock

class JsBlockBuilder : JsNodeBuilder<JsBlock>() {

    override fun build(): JsBlock = JsBlock(nodes)
}
