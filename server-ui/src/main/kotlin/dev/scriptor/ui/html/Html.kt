package dev.scriptor.ui.html

import dev.scriptor.ui.BuilderContext
import dev.scriptor.ui.dom.Document
import dev.scriptor.ui.html.builder.HtmlDocumentBuilder

context(_: BuilderContext)
fun html(block: HtmlDocumentBuilder.() -> Unit): Document {
    val builder = HtmlDocumentBuilder()
    builder.apply(block)
    return builder.build()
}
