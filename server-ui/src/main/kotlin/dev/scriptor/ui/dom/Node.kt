package dev.scriptor.ui.dom

sealed interface Node {

    fun toXmlString(): String
}
