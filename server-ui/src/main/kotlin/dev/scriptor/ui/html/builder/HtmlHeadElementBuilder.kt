package dev.scriptor.ui.html.builder

import dev.scriptor.ui.Bundle
import dev.scriptor.ui.html.HtmlElement

class HtmlHeadElementBuilder : HtmlElementBuilder(
    false,
    "head",
    listOf(),
) {

    context(_: Bundle)
    fun title(content: String): HtmlElement {
        return element(false, "title") { +content }
    }

    context(_: Bundle)
    fun meta(
        charset: String? = null,
        content: String? = null,
        httpEquiv: String? = null,
        media: String? = null,
        name: String? = null,
    ): HtmlElement {
        return element(true, "meta", {
            this["charset"] = charset
            this["content"] = content
            this["http-equiv"] = httpEquiv
            this["media"] = media
            this["name"] = name
        })
    }

    context(_: Bundle)
    fun link(
        httpAs: String? = null,
        blocking: String? = null,
        crossOrigin: String? = null,
        disabled: Boolean = false,
        fetchPriority: String? = null,
        href: String? = null,
        hrefLang: String? = null,
        imageSizes: String? = null,
        imageSrcSet: String? = null,
        integrity: String? = null,
        media: String? = null,
        referrerPolicy: String? = null,
        rel: String? = null,
        sizes: String? = null,
        title: String? = null,
        type: String? = null,
    ): HtmlElement {
        return element(true, "link", {
            this["as"] = httpAs
            this["blocking"] = blocking
            this["crossorigin"] = crossOrigin
            this["disabled"] = disabled
            this["fetchpriority"] = fetchPriority
            this["href"] = href
            this["hreflang"] = hrefLang
            this["imagesizes"] = imageSizes
            this["imagesrcset"] = imageSrcSet
            this["integrity"] = integrity
            this["media"] = media
            this["referrerpolicy"] = referrerPolicy
            this["rel"] = rel
            this["sizes"] = sizes
            this["title"] = title
            this["type"] = type
        })
    }
}
