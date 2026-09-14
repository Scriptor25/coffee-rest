package dev.scriptor.ui.dom

data class Attribute(val name: String, val value: String?) {
    fun toXmlString(): String = when (value) {
        null -> name
        else -> """$name="$value""""
    }
}
