package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.dom.Attribute

abstract class AttributeBuilder {

    private sealed interface AttributeValue

    private data object BooleanAttributeValue : AttributeValue
    private data class StringAttributeValue(val value: String) : AttributeValue

    private val values = mutableMapOf<String, AttributeValue?>()

    protected fun boolean(name: String): Boolean =
        when (val value = values[name]) {
            is BooleanAttributeValue -> true
            else -> false
        }

    protected fun string(name: String): String? =
        when (val value = values[name]) {
            is StringAttributeValue -> value.value
            else -> null
        }

    protected fun <E : Enum<E>> enum(name: String, parse: (String) -> E?): E? =
        when (val value = values[name]) {
            is StringAttributeValue -> parse(value.value)
            else -> null
        }

    protected fun boolean(name: String, value: Boolean) {
        values[name] = when (value) {
            true -> BooleanAttributeValue
            else -> null
        }
    }

    protected fun string(name: String, value: String?) {
        values[name] = when (value) {
            is String -> StringAttributeValue(value)
            else -> null
        }
    }

    protected fun <E : Enum<E>> enum(name: String, value: E?, get: E.() -> String) {
        values[name] = when (value) {
            is E -> StringAttributeValue(value.get())
            else -> null
        }
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
