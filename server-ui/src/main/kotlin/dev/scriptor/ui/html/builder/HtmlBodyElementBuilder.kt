package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.Attribute

class HtmlBodyElementBuilder(
    attributes: List<Attribute>,
) : HtmlGenericElementBuilder(false, "body", attributes)
