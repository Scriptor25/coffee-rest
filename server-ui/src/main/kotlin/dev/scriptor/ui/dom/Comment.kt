package dev.scriptor.ui.dom

data class Comment(val content: String) : Node {

    override fun toXmlString(): String = """<!-- $content -->"""
}
