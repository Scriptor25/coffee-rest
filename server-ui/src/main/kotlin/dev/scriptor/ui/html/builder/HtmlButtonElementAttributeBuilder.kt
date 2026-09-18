package dev.scriptor.ui.html.builder

enum class HtmlButtonElementCommand(val value: String) {
    SHOW_MODAL("show-modal"),
    CLOSE("close"),
    REQUEST_CLOSE("request-close"),
    SHOW_POPOVER("show-popover"),
    HIDE_POPOVER("hide-popover"),
    TOGGLE_POPOVER("toggle-popover");

    companion object {
        fun parse(value: String): HtmlButtonElementCommand? =
            when (value) {
                SHOW_MODAL.value -> SHOW_MODAL
                CLOSE.value -> CLOSE
                REQUEST_CLOSE.value -> REQUEST_CLOSE
                SHOW_POPOVER.value -> SHOW_POPOVER
                HIDE_POPOVER.value -> HIDE_POPOVER
                TOGGLE_POPOVER.value -> TOGGLE_POPOVER
                else -> null
            }
    }
}

enum class HtmlButtonElementType(val value: String) {
    SUBMIT("submit"),
    RESET("reset"),
    BUTTON("button");

    companion object {
        fun parse(value: String): HtmlButtonElementType? =
            when (value) {
                SUBMIT.value -> SUBMIT
                RESET.value -> RESET
                BUTTON.value -> BUTTON
                else -> null
            }
    }
}

class HtmlButtonElementAttributeBuilder : HtmlFormChildAttributeBuilder() {

    var autoFocus by boolean("autofocus")
    var command by enum(
        "command",
        HtmlButtonElementCommand::parse,
        HtmlButtonElementCommand::value,
    )
    var customCommand by string("command")
    var commandFor by string("commandfor")
    var type by enum(
        "type",
        HtmlButtonElementType::parse,
        HtmlButtonElementType::value,
    )
}
