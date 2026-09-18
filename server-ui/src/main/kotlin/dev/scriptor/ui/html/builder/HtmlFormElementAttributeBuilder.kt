package dev.scriptor.ui.html.builder

class HtmlFormElementAttributeBuilder : HtmlAttributeBuilder() {

    var acceptCharset by string("accept-charset")
    var autoComplete by string("autocomplete")
    var name by string("name")
    var rel by string("rel")
    var action by string("action")
    var encType by string("enctype")
    var method by string("method")
    var noValidate by boolean("novalidate")
    var target by string("target")
}
