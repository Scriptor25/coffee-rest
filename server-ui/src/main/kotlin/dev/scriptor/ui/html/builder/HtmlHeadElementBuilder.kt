package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.html.HtmlElement

class HtmlHeadElementBuilder : HtmlElementBuilder(false, "head", listOf()) {

    context(_: Bundle)
    fun title(content: String): HtmlElement {
        return element(false, "title") { +content }
    }

    context(_: Bundle)
    fun meta(
        charset: String? = null,
        content: String? = null,
        httpEquiv: String? = null,
        media: String? = null,
        name: String? = null,
    ): HtmlElement {
        return element(true, "meta") {}
    }
}
