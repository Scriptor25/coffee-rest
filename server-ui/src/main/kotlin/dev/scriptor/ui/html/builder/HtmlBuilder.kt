package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.builder.Builder
import dev.scriptor.ui.html.HtmlElement

interface HtmlBuilder<T> : Builder<T> {

    fun <E : HtmlElement, B : HtmlElementBuilder<E>> element(
        builder: B,
        block: B.() -> Unit,
    ): E {
        builder.apply(block)
        val element = builder.build()
        children += element
        return element
    }
}
