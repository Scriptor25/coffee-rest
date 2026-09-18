package dev.scriptor.ui.dom

data class Raw(val content: String) : Node {

    override fun toXmlString(): String = content
}
