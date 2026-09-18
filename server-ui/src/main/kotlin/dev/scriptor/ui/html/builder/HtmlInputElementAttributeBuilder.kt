package dev.scriptor.ui.html.builder

enum class HtmlInputElementCapture(val value: String) {
    USER("user"),
    ENVIRONMENT("environment");

    companion object {
        fun parse(value: String): HtmlInputElementCapture? =
            when (value) {
                USER.value -> USER
                ENVIRONMENT.value -> ENVIRONMENT
                else -> null
            }
    }
}

enum class HtmlInputElementColorSpace(val value: String) {
    LIMITED_SRGB("limited-srgb"),
    DISPLAY_P3("display-p3");

    companion object {
        fun parse(value: String): HtmlInputElementColorSpace? =
            when (value) {
                LIMITED_SRGB.value -> LIMITED_SRGB
                DISPLAY_P3.value -> DISPLAY_P3
                else -> null
            }
    }
}

enum class HtmlInputElementType(val value: String) {
    BUTTON("button"),
    CHECKBOX("checkbox"),
    COLOR("color"),
    DATE("date"),
    DATETIME_LOCAL("datetime-local"),
    EMAIL("email"),
    FILE("file"),
    HIDDEN("hidden"),
    IMAGE("image"),
    MONTH("month"),
    NUMBER("number"),
    PASSWORD("password"),
    RADIO("radio"),
    RANGE("range"),
    RESET("reset"),
    SEARCH("search"),
    SUBMIT("submit"),
    TEL("tel"),
    TEXT("text"),
    TIME("time"),
    URL("url"),
    WEEK("week");

    companion object {
        fun parse(value: String): HtmlInputElementType? =
            when (value) {
                BUTTON.value -> BUTTON
                CHECKBOX.value -> CHECKBOX
                COLOR.value -> COLOR
                DATE.value -> DATE
                DATETIME_LOCAL.value -> DATETIME_LOCAL
                EMAIL.value -> EMAIL
                FILE.value -> FILE
                HIDDEN.value -> HIDDEN
                IMAGE.value -> IMAGE
                MONTH.value -> MONTH
                NUMBER.value -> NUMBER
                PASSWORD.value -> PASSWORD
                RADIO.value -> RADIO
                RANGE.value -> RANGE
                RESET.value -> RESET
                SEARCH.value -> SEARCH
                SUBMIT.value -> SUBMIT
                TEL.value -> TEL
                TEXT.value -> TEXT
                TIME.value -> TIME
                URL.value -> URL
                WEEK.value -> WEEK
                else -> null
            }
    }
}

class HtmlInputElementAttributeBuilder : HtmlFormChildAttributeBuilder() {

    var accept by string("accept")
    var alpha by string("alpha")
    var alt by string("alt")
    var autoComplete by string("autocomplete")
    var capture by enum(
        "capture",
        HtmlInputElementCapture::parse,
        HtmlInputElementCapture::value,
    )
    var checked by boolean("checked")
    var colorSpace by enum(
        "colorspace",
        HtmlInputElementColorSpace::parse,
        HtmlInputElementColorSpace::value,
    )
    var dirName by string("dirname")
    var height by other(
        "height",
        String::toIntOrNull,
        Int::toString,
    )
    var list by string("list")
    var max by string("max")
    var maxLength by other(
        "maxlength",
        String::toIntOrNull,
        Int::toString,
    )
    var min by string("min")
    var minLength by other(
        "minlength",
        String::toIntOrNull,
        Int::toString,
    )
    var multiple by boolean("multiple")
    var pattern by string("pattern")
    var placeholder by string("placeholder")
    var readonly by boolean("readonly")
    var required by boolean("required")
    var size by other(
        "size",
        String::toIntOrNull,
        Int::toString,
    )
    var src by string("src")
    var step by string("step")
    var switch by boolean("switch")
    var type by other(
        "type",
        HtmlInputElementType::parse,
        HtmlInputElementType::value,
    )
    var width by other(
        "width",
        String::toIntOrNull,
        Int::toString,
    )
}
