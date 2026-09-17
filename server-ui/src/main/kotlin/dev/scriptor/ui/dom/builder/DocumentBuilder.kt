package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.dom.Node

class DocumentBuilder(val type: String) : Builder<Document> {

    override val children = mutableListOf<Node>()

    context(context: Bundle)
    override fun build(): Document {
        return Document(
            type,
            children,
        )
    }
}
