package dev.scriptor.ui.dom

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.builder.DocumentBuilder

context(_: Bundle)
fun document(type: String, block: DocumentBuilder.() -> Unit): Document {
    val builder = DocumentBuilder(type)
    builder.apply(block)
    return builder.build()
}

fun escapeText(content: String, vararg replace: Char): String =
    buildString(content.length) {
        for (char in content) {
            if (char !in replace) {
                append(char)
                continue
            }

            when (char) {
                '&' -> append("&amp;")
                '<' -> append("&lt;")
                '>' -> append("&gt;")
                '\'' -> append("&apos;")
                '"' -> append("&quot;")
                else -> error("undefined escape sequence for '$char'")
            }
        }
    }
