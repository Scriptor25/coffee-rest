package dev.scriptor.ui.html.builder

class HtmlAnchorElementAttributeBuilder : HtmlAttributeBuilder() {

    var href: String?
        get() = string("href")
        set(value) {
            string("href", value)
        }
}
