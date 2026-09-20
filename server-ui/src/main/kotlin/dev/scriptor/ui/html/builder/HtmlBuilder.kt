package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.dom.builder.Builder
import dev.scriptor.ui.dom.builder.WithBundle

interface HtmlBuilder<T> : Builder<T> {

    context(_: Bundle)
    fun <B : HtmlElementBuilder> element(
        builder: B,
        block: WithBundle<B>,
    ) {
        builder.block()
        children += builder.build()
    }
}
