package dev.scriptor.ui.html.builder

import dev.scriptor.ui.dom.Attribute

enum class HtmlAutoCapitalize(val value: String) {
    NONE("none"),
    SENTENCES("sentences"),
    WORDS("words"),
    CHARACTERS("characters"),
}

enum class HtmlAutoCorrect(val value: String) {
    ON("on"),
    OFF("off"),
}

enum class HtmlContentEditable(val value: String) {
    TRUE("true"),
    FALSE("false"),
    PLAINTEXT_ONLY("plaintext-only"),
}

enum class HtmlDir(val value: String) {
    LTR("ltr"),
    RTL("rtl"),
    AUTO("auto"),
}

enum class HtmlDraggable(val value: String) {
    TRUE("true"),
    FALSE("false"),
}

enum class HtmlEnterKeyHint(val value: String) {
    ENTER("enter"),
    DONE("done"),
    GO("go"),
    NEXT("next"),
    PREVIOUS("previous"),
    SEARCH("search"),
    SEND("send"),
}

enum class HtmlHidden(val value: String) {
    HIDDEN("hidden"),
    UNTIL_FOUND("until-found")
}

enum class HtmlInputMode(val value: String) {
    NONE("none"),
    TEXT("text"),
    DECIMAL("decimal"),
    NUMERIC("numeric"),
    TEL("tel"),
    SEARCH("search"),
    EMAIL("email"),
    URL("url"),
}

enum class HtmlPopover(val value: String) {
    AUTO("auto"),
    HINT("hint"),
    MANUAL("manual"),
}

enum class HtmlRole(val value: String) {}

enum class HtmlSpellCheck(val value: String) {
    TRUE("true"),
    FALSE("false"),
}

enum class HtmlTranslate(val value: String) {
    YES("yes"),
    NO("no"),
}

enum class HtmlVirtualKeyboardPolicy(val value: String) {
    AUTO("auto"),
    MANUAL("manual"),
}

enum class HtmlWritingSuggestions(val value: String) {
    TRUE("true"),
    FALSE("false"),
}

open class HtmlGlobalAttributeBuilder {

    var accesskey: String? = null
    var anchor: String? = null
    var autocapitalize: HtmlAutoCapitalize? = null
    var autocorrect: HtmlAutoCorrect? = null
    var autofocus: Boolean? = null
    var class_: String? = null
    var contenteditable: HtmlContentEditable? = null
    var data = mutableMapOf<String, String?>()
    var dir: HtmlDir? = null
    var draggable: HtmlDraggable? = null
    var enterkeyhint: HtmlEnterKeyHint? = null
    var exportparts: String? = null
    var headingoffset: Int? = null
    var headingreset: Boolean? = null
    var hidden: HtmlHidden? = null
    var id: String? = null
    var inert: Boolean? = null
    var inputmode: HtmlInputMode? = null
    var is_: String? = null
    var itemid: String? = null
    var itemprop: String? = null
    var itemref: String? = null
    var itemscope: Boolean? = null
    var itemtype: String? = null
    var lang: String? = null
    var nonce: String? = null
    var part: String? = null
    var popover: HtmlPopover? = null
    var role: HtmlRole? = null
    var slot: String? = null
    var spellcheck: HtmlSpellCheck? = null
    var style: String? = null
    var tabindex: Int? = null
    var title: String? = null
    var translate: HtmlTranslate? = null
    var virtualkeyboardpolicy: HtmlVirtualKeyboardPolicy? = null
    var writingsuggestions: HtmlWritingSuggestions? = null

    var onabort: Unit? = null
    var onanimationcancel: Unit? = null
    var onanimationend: Unit? = null
    var onanimationiteration: Unit? = null
    var onanimationstart: Unit? = null
    var onauxclick: Unit? = null
    var onbeforeinput: Unit? = null
    var onbeforematch: Unit? = null
    var onbeforetoggle: Unit? = null
    var onblur: Unit? = null
    var oncancel: Unit? = null
    var oncanplay: Unit? = null
    var oncanplaythrough: Unit? = null
    var onchange: Unit? = null
    var onclick: Unit? = null
    var onclose: Unit? = null
    var oncommand: Unit? = null
    var oncontentvisibilityautostatechange: Unit? = null
    var oncontextlost: Unit? = null
    var oncontextmenu: Unit? = null
    var oncontextrestored: Unit? = null
    var oncopy: Unit? = null
    var oncuechange: Unit? = null
    var oncut: Unit? = null
    var ondblclick: Unit? = null
    var ondrag: Unit? = null
    var ondragend: Unit? = null
    var ondragenter: Unit? = null
    var ondragleave: Unit? = null
    var ondragover: Unit? = null
    var ondragstart: Unit? = null
    var ondrop: Unit? = null
    var ondurationchange: Unit? = null
    var onemptied: Unit? = null
    var onended: Unit? = null
    var onerror: Unit? = null
    var onfocus: Unit? = null
    var onfocusin: Unit? = null
    var onfocusout: Unit? = null
    var onformdata: Unit? = null
    var onfullscreenchange: Unit? = null
    var onfullscreenerror: Unit? = null
    var ongesturechange: Unit? = null
    var ongestureend: Unit? = null
    var ongesturestart: Unit? = null
    var ongotpointercapture: Unit? = null
    var oninput: Unit? = null
    var oninvalid: Unit? = null
    var onkeydown: Unit? = null
    var onkeypress: Unit? = null
    var onkeyup: Unit? = null
    var onload: Unit? = null
    var onloadeddata: Unit? = null
    var onloadedmetadata: Unit? = null
    var onloadstart: Unit? = null
    var onlostpointercapture: Unit? = null
    var onmousedown: Unit? = null
    var onmouseenter: Unit? = null
    var onmouseleave: Unit? = null
    var onmousemove: Unit? = null
    var onmouseout: Unit? = null
    var onmouseover: Unit? = null
    var onmouseup: Unit? = null
    var onmousewheel: Unit? = null
    var onpaste: Unit? = null
    var onpause: Unit? = null
    var onplay: Unit? = null
    var onplaying: Unit? = null
    var onpointercancel: Unit? = null
    var onpointerdown: Unit? = null
    var onpointerenter: Unit? = null
    var onpointerleave: Unit? = null
    var onpointermove: Unit? = null
    var onpointerout: Unit? = null
    var onpointerover: Unit? = null
    var onpointerrawupdate: Unit? = null
    var onpointerup: Unit? = null
    var onprogress: Unit? = null
    var onratechange: Unit? = null
    var onreset: Unit? = null
    var onresize: Unit? = null
    var onscroll: Unit? = null
    var onscrollend: Unit? = null
    var onscrollsnapchange: Unit? = null
    var onscrollsnapchanging: Unit? = null
    var onsecuritypolicyviolation: Unit? = null
    var onseeked: Unit? = null
    var onseeking: Unit? = null
    var onselect: Unit? = null
    var onselectionchange: Unit? = null
    var onselectstart: Unit? = null
    var onslotchange: Unit? = null
    var onstalled: Unit? = null
    var onsubmit: Unit? = null
    var onsuspend: Unit? = null
    var ontimeupdate: Unit? = null
    var ontoggle: Unit? = null
    var ontouchcancel: Unit? = null
    var ontouchend: Unit? = null
    var ontouchmove: Unit? = null
    var ontouchstart: Unit? = null
    var ontransitioncancel: Unit? = null
    var ontransitionend: Unit? = null
    var ontransitionrun: Unit? = null
    var ontransitionstart: Unit? = null
    var onvolumechange: Unit? = null
    var onwaiting: Unit? = null
    var onwebkitmouseforcechanged: Unit? = null
    var onwebkitmouseforcedown: Unit? = null
    var onwebkitmouseforceup: Unit? = null
    var onwebkitmouseforcewillbegin: Unit? = null
    var onwheel: Unit? = null

    private sealed interface AttributeValue

    private data object BooleanAttributeValue : AttributeValue
    private data class StringAttributeValue(val value: String) : AttributeValue

    private fun boolean(value: Boolean?): BooleanAttributeValue? = when (value) {
        null, false -> null
        else -> BooleanAttributeValue
    }

    private fun string(value: String?): StringAttributeValue? = when (value) {
        null -> null
        else -> StringAttributeValue(value)
    }

    fun build(): List<Attribute> = listOf<Pair<String, AttributeValue?>>(
        "accesskey" to string(accesskey),
        "anchor" to string(anchor),
        "autocapitalize" to string(autocapitalize?.value),
        "autocorrect" to string(autocorrect?.value),
        "autofocus" to boolean(autofocus),
        "class" to string(class_),
        "contenteditable" to string(contenteditable?.value),
        // TODO: "data" to data,
        "dir" to string(dir?.value),
        "draggable" to string(draggable?.value),
        "enterkeyhint" to string(enterkeyhint?.value),
        "exportparts" to string(exportparts),
        "headingoffset" to string(headingoffset?.toString()),
        "headingreset" to string(headingreset?.toString()),
        "hidden" to string(hidden?.value),
        "id" to string(id),
        "inert" to boolean(inert),
        "inputmode" to string(inputmode?.value),
        "is" to string(is_),
        "itemid" to string(itemid),
        "itemprop" to string(itemprop),
        "itemref" to string(itemref),
        "itemscope" to boolean(itemscope),
        "itemtype" to string(itemtype),
        "lang" to string(lang),
        "nonce" to string(nonce),
        "part" to string(part),
        "popover" to string(popover?.value),
        "role" to string(role?.value),
        "slot" to string(slot),
        "spellcheck" to string(spellcheck?.value),
        "style" to string(style),
        "tabindex" to string(tabindex?.toString()),
        "title" to string(title),
        "translate" to string(translate?.value),
        "virtualkeyboardpolicy" to string(virtualkeyboardpolicy?.value),
        "writingsuggestions" to string(writingsuggestions?.value),
    )
        .mapNotNull {
            when (val value = it.second) {
                is StringAttributeValue -> Attribute(it.first, value.value)
                is BooleanAttributeValue -> Attribute(it.first, null)
                else -> null
            }
        }
}