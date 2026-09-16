package dev.scriptor.ui.html.builder

import dev.scriptor.ui.BuilderContext
import dev.scriptor.ui.dom.builder.Builder
import dev.scriptor.ui.html.HtmlElement

interface HtmlBuilder<T> : Builder<T> {

    context(_: BuilderContext)
    fun <B : HtmlElementBuilder> element(
        builder: B,
        block: B.() -> Unit,
    ): HtmlElement {
        builder.apply(block)
        val element = builder.build()
        children += element
        return element
    }
}
