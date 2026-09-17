package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.html.HtmlElement

class HtmlHeadElementBuilder : HtmlElementBuilder(false, "head", listOf()) {

    context(_: Bundle)
    fun title(content: String): HtmlElement {
        return element(false, "title") { +content }
    }
}
