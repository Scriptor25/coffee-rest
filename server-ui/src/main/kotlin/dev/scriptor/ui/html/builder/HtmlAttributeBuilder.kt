package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.builder.AttributeBuilder

enum class HtmlAutoCapitalize(val value: String) {
    NONE("none"),
    SENTENCES("sentences"),
    WORDS("words"),
    CHARACTERS("characters");

    companion object {
        fun parse(value: String): HtmlAutoCapitalize? =
            when (value) {
                NONE.value -> NONE
                SENTENCES.value -> SENTENCES
                WORDS.value -> WORDS
                CHARACTERS.value -> CHARACTERS
                else -> null
            }
    }
}

enum class HtmlAutoCorrect(val value: String) {
    ON("on"),
    OFF("off");

    companion object {
        fun parse(value: String): HtmlAutoCorrect? =
            when (value) {
                ON.value -> ON
                OFF.value -> OFF
                else -> null
            }
    }
}

enum class HtmlContentEditable(val value: String) {
    TRUE("true"),
    FALSE("false"),
    PLAINTEXT_ONLY("plaintext-only");

    companion object {
        fun parse(value: String): HtmlContentEditable? =
            when (value) {
                TRUE.value -> TRUE
                FALSE.value -> FALSE
                PLAINTEXT_ONLY.value -> PLAINTEXT_ONLY
                else -> null
            }
    }
}

enum class HtmlDir(val value: String) {
    LTR("ltr"),
    RTL("rtl"),
    AUTO("auto");

    companion object {
        fun parse(value: String): HtmlDir? =
            when (value) {
                LTR.value -> LTR
                RTL.value -> RTL
                AUTO.value -> AUTO
                else -> null
            }
    }
}

enum class HtmlDraggable(val value: String) {
    TRUE("true"),
    FALSE("false");

    companion object {
        fun parse(value: String): HtmlDraggable? =
            when (value) {
                TRUE.value -> TRUE
                FALSE.value -> FALSE
                else -> null
            }
    }
}

enum class HtmlEnterKeyHint(val value: String) {
    ENTER("enter"),
    DONE("done"),
    GO("go"),
    NEXT("next"),
    PREVIOUS("previous"),
    SEARCH("search"),
    SEND("send");

    companion object {
        fun parse(value: String): HtmlEnterKeyHint? =
            when (value) {
                ENTER.value -> ENTER
                DONE.value -> DONE
                GO.value -> GO
                NEXT.value -> NEXT
                PREVIOUS.value -> PREVIOUS
                SEARCH.value -> SEARCH
                SEND.value -> SEND
                else -> null
            }
    }
}

enum class HtmlHidden(val value: String) {
    HIDDEN("hidden"),
    UNTIL_FOUND("until-found");

    companion object {
        fun parse(value: String): HtmlHidden? =
            when (value) {
                HIDDEN.value -> HIDDEN
                UNTIL_FOUND.value -> UNTIL_FOUND
                else -> null
            }
    }
}

enum class HtmlInputMode(val value: String) {
    NONE("none"),
    TEXT("text"),
    DECIMAL("decimal"),
    NUMERIC("numeric"),
    TEL("tel"),
    SEARCH("search"),
    EMAIL("email"),
    URL("url");

    companion object {
        fun parse(value: String): HtmlInputMode? =
            when (value) {
                NONE.value -> NONE
                TEXT.value -> TEXT
                DECIMAL.value -> DECIMAL
                NUMERIC.value -> NUMERIC
                TEL.value -> TEL
                SEARCH.value -> SEARCH
                EMAIL.value -> EMAIL
                URL.value -> URL
                else -> null
            }
    }
}

enum class HtmlPopover(val value: String) {
    AUTO("auto"),
    HINT("hint"),
    MANUAL("manual");

    companion object {
        fun parse(value: String): HtmlPopover? =
            when (value) {
                AUTO.value -> AUTO
                HINT.value -> HINT
                MANUAL.value -> MANUAL
                else -> null
            }
    }
}

enum class HtmlRole(val value: String) {
    ;

    companion object {
        fun parse(value: String): HtmlRole? =
            when (value) {
                else -> null
            }
    }
}

enum class HtmlSpellcheck(val value: String) {
    TRUE("true"),
    FALSE("false");

    companion object {
        fun parse(value: String): HtmlSpellcheck? =
            when (value) {
                TRUE.value -> TRUE
                FALSE.value -> FALSE
                else -> null
            }
    }
}

enum class HtmlTranslate(val value: String) {
    YES("yes"),
    NO("no");

    companion object {
        fun parse(value: String): HtmlTranslate? =
            when (value) {
                YES.value -> YES
                NO.value -> NO
                else -> null
            }
    }
}

enum class HtmlVirtualKeyboardPolicy(val value: String) {
    AUTO("auto"),
    MANUAL("manual");

    companion object {
        fun parse(value: String): HtmlVirtualKeyboardPolicy? =
            when (value) {
                AUTO.value -> AUTO
                MANUAL.value -> MANUAL
                else -> null
            }
    }
}

enum class HtmlWritingSuggestions(val value: String) {
    TRUE("true"),
    FALSE("false");

    companion object {
        fun parse(value: String): HtmlWritingSuggestions? =
            when (value) {
                TRUE.value -> TRUE
                FALSE.value -> FALSE
                else -> null
            }
    }
}

open class HtmlAttributeBuilder : AttributeBuilder() {

    var accessKey by string("accesskey")
    var anchor by string("anchor")
    var autoCapitalize by enum(
        "autocapitalize",
        HtmlAutoCapitalize::parse,
        HtmlAutoCapitalize::value,
    )
    var autoCorrect by enum(
        "autocorrect",
        HtmlAutoCorrect::parse,
        HtmlAutoCorrect::value,
    )
    var autofocus by boolean("autofocus")
    var htmlClass by string("class")
    var contentEditable by enum(
        "contenteditable",
        HtmlContentEditable::parse,
        HtmlContentEditable::value,
    )
    var dir by enum(
        "dir",
        HtmlDir::parse,
        HtmlDir::value,
    )
    var draggable by enum(
        "draggable",
        HtmlDraggable::parse,
        HtmlDraggable::value,
    )
    var enterKeyHint by enum(
        "enterkeyhint",
        HtmlEnterKeyHint::parse,
        HtmlEnterKeyHint::value,
    )
    var exportParts by string("exportparts")
    var headingOffset by other(
        "headingoffset",
        String::toIntOrNull,
        Int::toString,
    )
    var headingReset by boolean("headingreset")
    var hidden by enum(
        "hidden",
        HtmlHidden::parse,
        HtmlHidden::value,
    )
    var id by string("id")
    var inert by boolean("inert")
    var inputMode by enum(
        "inputmode",
        HtmlInputMode::parse,
        HtmlInputMode::value,
    )
    var htmlIs by string("is")
    var itemId by string("itemid")
    var itemProp by string("itemprop")
    var itemRef by string("itemref")
    var itemScope by boolean("itemscope")
    var itemType by string("itemtype")
    var lang by string("lang")
    var nonce by string("nonce")
    var part by string("part")
    var popover by enum(
        "popover",
        HtmlPopover::parse,
        HtmlPopover::value,
    )
    var role by enum(
        "role",
        HtmlRole::parse,
        HtmlRole::value,
    )
    var slot by string("slot")
    var spellcheck by enum(
        "spellcheck",
        HtmlSpellcheck::parse,
        HtmlSpellcheck::value,
    )
    var style by string("style")
    var tabIndex by other(
        "tabindex",
        String::toIntOrNull,
        Int::toString,
    )
    var title by string("title")
    var translate by enum(
        "translate",
        HtmlTranslate::parse,
        HtmlTranslate::value,
    )
    var virtualKeyboardPolicy by enum(
        "virtualkeyboardpolicy",
        HtmlVirtualKeyboardPolicy::parse,
        HtmlVirtualKeyboardPolicy::value
    )
    var writingSuggestions by enum(
        "writingsuggestions",
        HtmlWritingSuggestions::parse,
        HtmlWritingSuggestions::value
    )

    fun stringData(name: String): String? {
        return getString("data-$name")
    }

    fun stringData(name: String, value: String?) {
        setString("data-$name", value)
    }

    fun booleanData(name: String): Boolean {
        return getBoolean("data-$name")
    }

    fun booleanData(name: String, value: Boolean) {
        setBoolean("data-$name", value)
    }
}