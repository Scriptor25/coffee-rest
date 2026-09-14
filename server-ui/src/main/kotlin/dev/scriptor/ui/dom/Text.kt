package dev.scriptor.ui.dom

data class Text(val content: String) : Node {

    override fun toXmlString(): String = escapeText(content)
}
