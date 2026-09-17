package dev.scriptor.ui

import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.dom.builder.DocumentBuilder
import dev.scriptor.ui.html.builder.HtmlDocumentBuilder
import dev.scriptor.ui.js.builder.JsNodesBuilder

class Bundle {

    val script = JsNodesBuilder()

    fun html(block: context(Bundle) HtmlDocumentBuilder.() -> Unit): Document {
        val builder = HtmlDocumentBuilder()
        builder.block()
        return builder.build()
    }

    fun document(type: String, block: context(Bundle) DocumentBuilder.() -> Unit): Document {
        val builder = DocumentBuilder(type)
        builder.block()
        return builder.build()
    }
}
