package dev.scriptor.ui.html

import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.Node

class HtmlTitleElement(
    attributes: List<Attribute>,
    children: List<Node>,
) : HtmlElement(
    false,
    "title",
    attributes,
    children,
)
