package dev.scriptor.ui.js

data class JsParameter(
    val name: String,
    val collect: Boolean,
) {

    fun toJsString(): String =
        "${if (collect) "..." else ""}$name"
}
