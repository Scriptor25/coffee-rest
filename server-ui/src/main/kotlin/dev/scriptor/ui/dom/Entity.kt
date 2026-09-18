package dev.scriptor.ui.dom

@JvmInline
value class Entity(val name: String) : Node {

    override fun toXmlString(): String = "&$name;"
}
