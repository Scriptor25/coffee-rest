package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.builder.AttributeBuilder

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

open class HtmlAttributeBuilder : AttributeBuilder() {

    var accessKey: String?
        get() = string("accesskey")
        set(value) {
            string("accesskey", value)
        }
    var anchor: String?
        get() = string("anchor")
        set(value) {
            string("anchor", value)
        }
    var autoCapitalize: HtmlAutoCapitalize?
        get() = enum("autocapitalize", HtmlAutoCapitalize::parse)
        set(value) {
            enum("autocapitalize", value, HtmlAutoCapitalize::value)
        }
    var autoCorrect: HtmlAutoCorrect?
        get() = enum("autocorrect", HtmlAutoCorrect::parse)
        set(value) {
            enum("autocorrect", value, HtmlAutoCorrect::value)
        }
    var autofocus: Boolean
        get() = boolean("autofocus")
        set(value) {
            boolean("autofocus", value)
        }
    var htmlClass: String?
        get() = string("class")
        set(value) {
            string("class", value)
        }
    var contentEditable: HtmlContentEditable?
        get() = enum("contenteditable", HtmlContentEditable::parse)
        set(value) {
            enum("contenteditable", value, HtmlContentEditable::value)
        }
    var dir: HtmlDir?
        get() = enum("dir", HtmlDir::parse)
        set(value) {
            enum("dir", value, HtmlDir::value)
        }
    var draggable: HtmlDraggable?
        get() = enum("draggable", HtmlDraggable::parse)
        set(value) {
            enum("draggable", value, HtmlDraggable::value)
        }
    var enterKeyHint: HtmlEnterKeyHint?
        get() = enum("enterkeyhint", HtmlEnterKeyHint::parse)
        set(value) {
            enum("enterkeyhint", value, HtmlEnterKeyHint::value)
        }
    var exportParts: String?
        get() = string("exportparts")
        set(value) {
            string("exportparts", value)
        }
    var headingOffset: Int?
        get() = string("headingoffset")?.toIntOrNull()
        set(value) {
            string("headingoffset", value?.toString())
        }
    var headingReset: Boolean
        get() = boolean("headingreset")
        set(value) {
            boolean("headingreset", value)
        }
    var hidden: HtmlHidden?
        get() = enum("hidden", HtmlHidden::parse)
        set(value) {
            enum("hidden", value, HtmlHidden::value)
        }
    var id: String?
        get() = string("id")
        set(value) {
            string("id", value)
        }
    var inert: Boolean
        get() = boolean("inert")
        set(value) {
            boolean("inert", value)
        }
    var inputMode: HtmlInputMode?
        get() = enum("inputmode", HtmlInputMode::parse)
        set(value) {
            enum("inputmode", value, HtmlInputMode::value)
        }
    var htmlIs: String?
        get() = string("is")
        set(value) {
            string("is", value)
        }
    var itemId: String?
        get() = string("itemid")
        set(value) {
            string("itemid", value)
        }
    var itemProp: String?
        get() = string("itemprop")
        set(value) {
            string("itemprop", value)
        }
    var itemRef: String?
        get() = string("itemref")
        set(value) {
            string("itemref", value)
        }
    var itemScope: Boolean
        get() = boolean("itemscope")
        set(value) {
            boolean("itemscope", value)
        }
    var itemType: String?
        get() = string("itemtype")
        set(value) {
            string("itemtype", value)
        }
    var lang: String?
        get() = string("lang")
        set(value) {
            string("lang", value)
        }
    var nonce: String?
        get() = string("nonce")
        set(value) {
            string("nonce", value)
        }
    var part: String?
        get() = string("part")
        set(value) {
            string("part", value)
        }
    var popover: HtmlPopover?
        get() = enum("popover", HtmlPopover::parse)
        set(value) {
            enum("popover", value, HtmlPopover::value)
        }
    var role: HtmlRole?
        get() = enum("role", HtmlRole::parse)
        set(value) {
            enum("role", value, HtmlRole::value)
        }
    var slot: String?
        get() = string("slot")
        set(value) {
            string("slot", value)
        }
    var spellcheck: HtmlSpellcheck?
        get() = enum("spellcheck", HtmlSpellcheck::parse)
        set(value) {
            enum("spellcheck", value, HtmlSpellcheck::value)
        }
    var style: String?
        get() = string("style")
        set(value) {
            string("style", value)
        }
    var tabIndex: Int?
        get() = string("tabindex")?.toIntOrNull()
        set(value) {
            string("tabindex", value?.toString())
        }
    var title: String?
        get() = string("title")
        set(value) {
            string("title", value)
        }
    var translate: HtmlTranslate?
        get() = enum("translate", HtmlTranslate::parse)
        set(value) {
            enum("translate", value, HtmlTranslate::value)
        }
    var virtualKeyboardPolicy: HtmlVirtualKeyboardPolicy?
        get() = enum("virtualkeyboardpolicy", HtmlVirtualKeyboardPolicy::parse)
        set(value) {
            enum("virtualkeyboardpolicy", value, HtmlVirtualKeyboardPolicy::value)
        }
    var writingSuggestions: HtmlWritingSuggestions?
        get() = enum("writingsuggestions", HtmlWritingSuggestions::parse)
        set(value) {
            enum("writingsuggestions", value, HtmlWritingSuggestions::value)
        }

    fun stringData(name: String): String? {
        return string("data-$name")
    }

    fun stringData(name: String, value: String?) {
        string("data-$name", value)
    }

    fun booleanData(name: String): Boolean {
        return boolean("data-$name")
    }

    fun booleanData(name: String, value: Boolean) {
        boolean("data-$name", value)
    }

    operator fun get(name: String): String? {
        return string(name)
    }

    operator fun contains(name: String): Boolean {
        return boolean(name)
    }

    operator fun set(name: String, value: String?) {
        string(name, value)
    }

    operator fun set(name: String, value: Boolean) {
        boolean(name, value)
    }
}