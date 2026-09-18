package dev.scriptor.ui.css.builder

import dev.scriptor.ui.css.CssNode

class CssNodesBuilder : CssBuilder<List<CssNode>>() {

    override fun build(): List<CssNode> {
        return nodes
    }
}
