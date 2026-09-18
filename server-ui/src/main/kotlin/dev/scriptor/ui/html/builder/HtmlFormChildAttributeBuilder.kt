package dev.scriptor.ui.html.builder

enum class HtmlFormMethod(val value: String) {
    POST("post"),
    GET("get"),
    DIALOG("dialog");

    companion object {
        fun parse(value: String): HtmlFormMethod? =
            when (value) {
                POST.value -> POST
                GET.value -> GET
                DIALOG.value -> DIALOG
                else -> null
            }
    }
}

enum class HtmlPopoverTargetAction(val value: String) {
    HIDE("hide"),
    SHOW("show"),
    TOGGLE("toggle");

    companion object {
        fun parse(value: String): HtmlPopoverTargetAction? =
            when (value) {
                HIDE.value -> HIDE
                SHOW.value -> SHOW
                TOGGLE.value -> TOGGLE
                else -> null
            }
    }
}

open class HtmlFormChildAttributeBuilder : HtmlAttributeBuilder() {

    var disabled by boolean("disabled")
    var form by string("form")
    var formAction by string("formaction")
    var formEncType by string("formenctype")
    var formMethod by enum(
        "formmethod",
        HtmlFormMethod::parse,
        HtmlFormMethod::value,
    )
    var formNoValidate by string("formnovalidate")
    var formTarget by string("formtarget")
    var name by string("name")
    var popoverTarget by string("popovertarget")
    var popoverTargetAction by enum(
        "popovertargetaction",
        HtmlPopoverTargetAction::parse,
        HtmlPopoverTargetAction::value,
    )
    var value by string("value")
}
