package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.builder.Builder
import dev.scriptor.ui.dom.builder.WithBundle
import dev.scriptor.ui.html.HtmlElement

interface HtmlBuilder<T> : Builder<T> {

    context(_: Bundle)
    fun <B : HtmlElementBuilder> element(
        builder: B,
        block: WithBundle<B>,
    ): HtmlElement {
        builder.block()
        val element = builder.build()
        children += element
        return element
    }
}
