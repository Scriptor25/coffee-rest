package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.JsBlock

class JsBlockBuilder : JsBuilder<JsBlock>() {

    override fun build(): JsBlock {
        return JsBlock(nodes)
    }
}
