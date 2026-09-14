package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.Text
import dev.scriptor.ui.html.HtmlHeadElement
import dev.scriptor.ui.html.HtmlTitleElement

class HtmlHeadElementBuilder : HtmlElementBuilder<HtmlHeadElement>(::HtmlHeadElement) {

    fun title(content: String): HtmlTitleElement {
        val element = HtmlTitleElement(listOf(), listOf(Text(content)))
        children += element
        return element
    }
}
