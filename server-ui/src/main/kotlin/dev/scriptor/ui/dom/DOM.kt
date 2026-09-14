package dev.scriptor.ui.dom

import dev.scriptor.ui.dom.builder.DocumentBuilder

fun document(type: String, block: DocumentBuilder.() -> Unit): Document {
    val builder = DocumentBuilder(type)
    builder.apply(block)
    return builder.build()
}

fun escapeText(content: String): String = buildString(content.length) {
    for (char in content) {
        when (char) {
            '&' -> append("&amp;")
            '<' -> append("&lt;")
            '>' -> append("&gt;")
            else -> append(char)
        }
    }
}

fun escapeAttribute(content: String): String = buildString(content.length) {
    for (char in content) {
        when (char) {
            '"' -> append("&quot;")
            '&' -> append("&amp;")
            '<' -> append("&lt;")
            '>' -> append("&gt;")
            else -> append(char)
        }
    }
}
