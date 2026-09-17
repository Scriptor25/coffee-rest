package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.Attribute

enum class HtmlAutoCapitalize(val value: String) {
    NONE("none"),
    SENTENCES("sentences"),
    WORDS("words"),
    CHARACTERS("characters");

    companion object {
        fun parse(value: String): HtmlAutoCapitalize = when (value) {
            NONE.value -> NONE
            SENTENCES.value -> SENTENCES
            WORDS.value -> WORDS
            CHARACTERS.value -> CHARACTERS
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlAutoCorrect(val value: String) {
    ON("on"),
    OFF("off");

    companion object {
        fun parse(value: String): HtmlAutoCorrect = when (value) {
            ON.value -> ON
            OFF.value -> OFF
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlContentEditable(val value: String) {
    TRUE("true"),
    FALSE("false"),
    PLAINTEXT_ONLY("plaintext-only");

    companion object {
        fun parse(value: String): HtmlContentEditable = when (value) {
            TRUE.value -> TRUE
            FALSE.value -> FALSE
            PLAINTEXT_ONLY.value -> PLAINTEXT_ONLY
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlDir(val value: String) {
    LTR("ltr"),
    RTL("rtl"),
    AUTO("auto");

    companion object {
        fun parse(value: String): HtmlDir = when (value) {
            LTR.value -> LTR
            RTL.value -> RTL
            AUTO.value -> AUTO
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlDraggable(val value: String) {
    TRUE("true"),
    FALSE("false");

    companion object {
        fun parse(value: String): HtmlDraggable = when (value) {
            TRUE.value -> TRUE
            FALSE.value -> FALSE
            else -> error("unexpected value '$value'")
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
        fun parse(value: String): HtmlEnterKeyHint = when (value) {
            ENTER.value -> ENTER
            DONE.value -> DONE
            GO.value -> GO
            NEXT.value -> NEXT
            PREVIOUS.value -> PREVIOUS
            SEARCH.value -> SEARCH
            SEND.value -> SEND
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlHidden(val value: String) {
    HIDDEN("hidden"),
    UNTIL_FOUND("until-found");

    companion object {
        fun parse(value: String): HtmlHidden = when (value) {
            HIDDEN.value -> HIDDEN
            UNTIL_FOUND.value -> UNTIL_FOUND
            else -> error("unexpected value '$value'")
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
        fun parse(value: String): HtmlInputMode = when (value) {
            NONE.value -> NONE
            TEXT.value -> TEXT
            DECIMAL.value -> DECIMAL
            NUMERIC.value -> NUMERIC
            TEL.value -> TEL
            SEARCH.value -> SEARCH
            EMAIL.value -> EMAIL
            URL.value -> URL
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlPopover(val value: String) {
    AUTO("auto"),
    HINT("hint"),
    MANUAL("manual");

    companion object {
        fun parse(value: String): HtmlPopover = when (value) {
            AUTO.value -> AUTO
            HINT.value -> HINT
            MANUAL.value -> MANUAL
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlRole(val value: String) {
    ;

    companion object {
        fun parse(value: String): HtmlRole = when (value) {
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlSpellcheck(val value: String) {
    TRUE("true"),
    FALSE("false");

    companion object {
        fun parse(value: String): HtmlSpellcheck = when (value) {
            TRUE.value -> TRUE
            FALSE.value -> FALSE
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlTranslate(val value: String) {
    YES("yes"),
    NO("no");

    companion object {
        fun parse(value: String): HtmlTranslate = when (value) {
            YES.value -> YES
            NO.value -> NO
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlVirtualKeyboardPolicy(val value: String) {
    AUTO("auto"),
    MANUAL("manual");

    companion object {
        fun parse(value: String): HtmlVirtualKeyboardPolicy = when (value) {
            AUTO.value -> AUTO
            MANUAL.value -> MANUAL
            else -> error("unexpected value '$value'")
        }
    }
}

enum class HtmlWritingSuggestions(val value: String) {
    TRUE("true"),
    FALSE("false");

    companion object {
        fun parse(value: String): HtmlWritingSuggestions = when (value) {
            TRUE.value -> TRUE
            FALSE.value -> FALSE
            else -> error("unexpected value '$value'")
        }
    }
}

open class HtmlGlobalAttributeBuilder {

    private val values = mutableMapOf<String, AttributeValue?>()

    var accessKey: String?
        get() = string(values["accesskey"])
        set(value) {
            values["accesskey"] = string(value)
        }
    var anchor: String?
        get() = string(values["anchor"])
        set(value) {
            values["anchor"] = string(value)
        }
    var autoCapitalize: HtmlAutoCapitalize?
        get() = enum(values["autocapitalize"], HtmlAutoCapitalize::parse)
        set(value) {
            values["autocapitalize"] = string(value?.value)
        }
    var autoCorrect: HtmlAutoCorrect?
        get() = enum(values["autocorrect"], HtmlAutoCorrect::parse)
        set(value) {
            values["autocorrect"] = string(value?.value)
        }
    var autofocus: Boolean
        get() = boolean(values["autofocus"])
        set(value) {
            values["autofocus"] = boolean(value)
        }
    var htmlClass: String?
        get() = string(values["class"])
        set(value) {
            values["class"] = string(value)
        }
    var contentEditable: HtmlContentEditable?
        get() = enum(values["contenteditable"], HtmlContentEditable::parse)
        set(value) {
            values["contenteditable"] = string(value?.value)
        }
    var dir: HtmlDir?
        get() = enum(values["dir"], HtmlDir::parse)
        set(value) {
            values["dir"] = string(value?.value)
        }
    var draggable: HtmlDraggable?
        get() = enum(values["draggable"], HtmlDraggable::parse)
        set(value) {
            values["draggable"] = string(value?.value)
        }
    var enterKeyHint: HtmlEnterKeyHint?
        get() = enum(values["enterkeyhint"], HtmlEnterKeyHint::parse)
        set(value) {
            values["enterkeyhint"] = string(value?.value)
        }
    var exportParts: String?
        get() = string(values["exportparts"])
        set(value) {
            values["exportparts"] = string(value)
        }
    var headingOffset: Int?
        get() = string(values["headingoffset"])?.toIntOrNull()
        set(value) {
            values["headingoffset"] = string(value?.toString())
        }
    var headingReset: Boolean
        get() = boolean(values["headingreset"])
        set(value) {
            values["headingreset"] = boolean(value)
        }
    var hidden: HtmlHidden?
        get() = enum(values["hidden"], HtmlHidden::parse)
        set(value) {
            values["hidden"] = string(value?.value)
        }
    var id: String?
        get() = string(values["id"])
        set(value) {
            values["id"] = string(value)
        }
    var inert: Boolean
        get() = boolean(values["inert"])
        set(value) {
            values["inert"] = boolean(value)
        }
    var inputMode: HtmlInputMode?
        get() = enum(values["inputmode"], HtmlInputMode::parse)
        set(value) {
            values["inputmode"] = string(value?.value)
        }
    var htmlIs: String?
        get() = string(values["is"])
        set(value) {
            values["is"] = string(value)
        }
    var itemId: String?
        get() = string(values["itemid"])
        set(value) {
            values["itemid"] = string(value)
        }
    var itemProp: String?
        get() = string(values["itemprop"])
        set(value) {
            values["itemprop"] = string(value)
        }
    var itemRef: String?
        get() = string(values["itemref"])
        set(value) {
            values["itemref"] = string(value)
        }
    var itemScope: Boolean
        get() = boolean(values["itemscope"])
        set(value) {
            values["itemscope"] = boolean(value)
        }
    var itemType: String?
        get() = string(values["itemtype"])
        set(value) {
            values["itemtype"] = string(value)
        }
    var lang: String?
        get() = string(values["lang"])
        set(value) {
            values["lang"] = string(value)
        }
    var nonce: String?
        get() = string(values["nonce"])
        set(value) {
            values["nonce"] = string(value)
        }
    var part: String?
        get() = string(values["part"])
        set(value) {
            values["part"] = string(value)
        }
    var popover: HtmlPopover?
        get() = enum(values["popover"], HtmlPopover::parse)
        set(value) {
            values["popover"] = string(value?.value)
        }
    var role: HtmlRole?
        get() = enum(values["role"], HtmlRole::parse)
        set(value) {
            values["role"] = string(value?.value)
        }
    var slot: String?
        get() = string(values["slot"])
        set(value) {
            values["slot"] = string(value)
        }
    var spellcheck: HtmlSpellcheck?
        get() = enum(values["spellcheck"], HtmlSpellcheck::parse)
        set(value) {
            values["spellcheck"] = string(value?.value)
        }
    var style: String?
        get() = string(values["style"])
        set(value) {
            values["style"] = string(value)
        }
    var tabIndex: Int?
        get() = string(values["tabindex"])?.toIntOrNull()
        set(value) {
            values["tabindex"] = string(value?.toString())
        }
    var title: String?
        get() = string(values["title"])
        set(value) {
            values["title"] = string(value)
        }
    var translate: HtmlTranslate?
        get() = enum(values["translate"], HtmlTranslate::parse)
        set(value) {
            values["translate"] = string(value?.value)
        }
    var virtualKeyboardPolicy: HtmlVirtualKeyboardPolicy?
        get() = enum(values["virtualkeyboardpolicy"], HtmlVirtualKeyboardPolicy::parse)
        set(value) {
            values["virtualkeyboardpolicy"] = string(value?.value)
        }
    var writingSuggestions: HtmlWritingSuggestions?
        get() = enum(values["writingsuggestions"], HtmlWritingSuggestions::parse)
        set(value) {
            values["writingsuggestions"] = string(value?.value)
        }

    fun stringData(name: String): String? {
        return string(values["data-$name"])
    }

    fun stringData(name: String, value: String?) {
        values["data-$name"] = string(value)
    }

    fun booleanData(name: String): Boolean {
        return boolean(values["data-$name"])
    }

    fun booleanData(name: String, value: Boolean) {
        values["data-$name"] = boolean(value)
    }

    operator fun get(name: String): String? {
        return string(values[name])
    }

    operator fun contains(name: String): Boolean {
        return boolean(values[name])
    }

    operator fun set(name: String, value: String?) {
        values[name] = string(value)
    }

    operator fun set(name: String, value: Boolean) {
        values[name] = boolean(value)
    }

    private sealed interface AttributeValue

    private data object BooleanAttributeValue : AttributeValue
    private data class StringAttributeValue(val value: String) : AttributeValue

    private fun boolean(value: Boolean): BooleanAttributeValue? = when (value) {
        true -> BooleanAttributeValue
        else -> null
    }

    private fun string(value: String?): StringAttributeValue? = when (value) {
        null -> null
        else -> StringAttributeValue(value)
    }

    private fun boolean(value: AttributeValue?): Boolean = when (value) {
        is BooleanAttributeValue -> true
        else -> false
    }

    private fun string(value: AttributeValue?): String? = when (value) {
        is StringAttributeValue -> value.value
        else -> null
    }

    private fun <E : Enum<E>> enum(value: AttributeValue?, transform: (String) -> E?): E? = when (value) {
        is StringAttributeValue -> transform(value.value)
        else -> null
    }

    fun build(): List<Attribute> = values
        .mapNotNull {
            when (val value = it.value) {
                is BooleanAttributeValue -> Attribute(it.key, null)
                is StringAttributeValue -> Attribute(it.key, value.value)
                else -> null
            }
        }
}