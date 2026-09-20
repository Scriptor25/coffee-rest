package dev.scriptor.ui.dom

sealed interface AttributeValue {

    data object BooleanValue : AttributeValue
    data class StringValue(val value: String) : AttributeValue
}

data class Attribute(val name: String, val value: AttributeValue) {

    constructor(pair: Pair<String, AttributeValue>) : this(pair.first, pair.second)

    fun toXmlString(): String = when (value) {
        AttributeValue.BooleanValue -> name
        is AttributeValue.StringValue -> "$name=\"${escapeText(value.value, '&', '<', '>', '\'', '"')}\""
    }
}
