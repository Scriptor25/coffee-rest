package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.dom.Node

class DocumentBuilder(val type: String) : Builder<Document> {

    override val children = mutableListOf<Node>()

    override fun build(): Document {
        return Document(
            type,
            children,
        )
    }
}
