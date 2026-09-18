package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.dom.Attribute
import kotlin.reflect.KProperty

open class AttributeBuilder {

    private sealed interface AttributeValue

    private data object BooleanAttributeValue : AttributeValue
    private data class StringAttributeValue(val value: String) : AttributeValue

    private val values = mutableMapOf<String, AttributeValue?>()

    protected fun getBoolean(name: String): Boolean =
        when (values[name]) {
            is BooleanAttributeValue -> true
            else -> false
        }

    protected fun getString(name: String): String? =
        when (val value = values[name]) {
            is StringAttributeValue -> value.value
            else -> null
        }

    protected fun <E : Enum<E>> getEnum(name: String, set: (String) -> E?): E? =
        when (val value = values[name]) {
            is StringAttributeValue -> set(value.value)
            else -> null
        }

    protected fun <T> getOther(name: String, set: (String) -> T?): T? =
        when (val value = values[name]) {
            is StringAttributeValue -> set(value.value)
            else -> null
        }

    protected fun setBoolean(name: String, value: Boolean) {
        values[name] = when (value) {
            true -> BooleanAttributeValue
            else -> null
        }
    }

    protected fun setString(name: String, value: String?) {
        values[name] = when (value) {
            is String -> StringAttributeValue(value)
            else -> null
        }
    }

    protected fun <E : Enum<E>> setEnum(name: String, value: E?, get: E.() -> String) {
        values[name] = when (value) {
            null -> null
            else -> StringAttributeValue(value.get())
        }
    }

    protected fun <T> setOther(name: String, value: T?, get: T.() -> String) {
        values[name] = when (value) {
            null -> null
            else -> StringAttributeValue(value.get())
        }
    }

    protected interface Delegate<T> {
        operator fun getValue(self: Any?, property: KProperty<*>): T
        operator fun setValue(self: Any?, property: KProperty<*>, value: T)
    }

    protected fun boolean(name: String): Delegate<Boolean> =
        object : Delegate<Boolean> {

            override fun getValue(self: Any?, property: KProperty<*>): Boolean {
                return getBoolean(name)
            }

            override fun setValue(self: Any?, property: KProperty<*>, value: Boolean) {
                return setBoolean(name, value)
            }
        }

    protected fun string(name: String): Delegate<String?> =
        object : Delegate<String?> {

            override fun getValue(self: Any?, property: KProperty<*>): String? {
                return getString(name)
            }

            override fun setValue(self: Any?, property: KProperty<*>, value: String?) {
                return setString(name, value)
            }
        }

    protected fun <E : Enum<E>> enum(name: String, set: (String) -> E?, get: E.() -> String): Delegate<E?> =
        object : Delegate<E?> {

            override fun getValue(self: Any?, property: KProperty<*>): E? {
                return getEnum(name, set)
            }

            override fun setValue(self: Any?, property: KProperty<*>, value: E?) {
                return setEnum(name, value, get)
            }
        }

    protected fun <T> other(name: String, set: (String) -> T?, get: T.() -> String): Delegate<T?> =
        object : Delegate<T?> {

            override fun getValue(self: Any?, property: KProperty<*>): T? {
                return getOther(name, set)
            }

            override fun setValue(self: Any?, property: KProperty<*>, value: T?) {
                return setOther(name, value, get)
            }
        }

    operator fun get(name: String): String? {
        return getString(name)
    }

    operator fun contains(name: String): Boolean {
        return getBoolean(name)
    }

    operator fun set(name: String, value: String?) {
        setString(name, value)
    }

    operator fun set(name: String, value: Boolean) {
        setBoolean(name, value)
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
