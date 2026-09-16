package dev.scriptor.ui.html.builder

import dev.scriptor.ui.html.HtmlElement

class HtmlHeadElementBuilder : HtmlElementBuilder(false, "head", listOf()) {

    fun title(content: String): HtmlElement {
        return element(false, "title") { +content }
    }
}
