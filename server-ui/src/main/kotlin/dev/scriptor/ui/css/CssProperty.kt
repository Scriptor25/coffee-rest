package dev.scriptor.ui.css

data class CssProperty(val name: String, val value: String) {

    fun toCssString(): String =
        "$name:$value"
}
