package dev.scriptor.ui.dom.builder

import dev.scriptor.ui.Delegate
import dev.scriptor.ui.dom.Attribute
import dev.scriptor.ui.dom.AttributeValue
import kotlin.reflect.KProperty

open class AttributeBuilder {

    private val values = mutableMapOf<String, AttributeValue?>()

    protected fun getBoolean(name: String): Boolean =
        when (values[name]) {
            AttributeValue.BooleanValue -> true
            else -> false
        }

    protected fun getString(name: String): String? =
        when (val value = values[name]) {
            is AttributeValue.StringValue -> value.value
            else -> null
        }

    protected fun <E : Enum<E>> getEnum(name: String, set: (String) -> E?): E? =
        when (val value = values[name]) {
            is AttributeValue.StringValue -> set(value.value)
            else -> null
        }

    protected fun <T> getOther(name: String, set: (String) -> T?): T? =
        when (val value = values[name]) {
            is AttributeValue.StringValue -> set(value.value)
            else -> null
        }

    protected fun setBoolean(name: String, value: Boolean) {
        values[name] = when (value) {
            true -> AttributeValue.BooleanValue
            else -> null
        }
    }

    protected fun setString(name: String, value: String?) {
        values[name] = when (value) {
            null -> null
            else -> AttributeValue.StringValue(value)
        }
    }

    protected fun <E : Enum<E>> setEnum(name: String, value: E?, get: E.() -> String) {
        values[name] = when (value) {
            null -> null
            else -> AttributeValue.StringValue(value.get())
        }
    }

    protected fun <T> setOther(name: String, value: T?, get: T.() -> String) {
        values[name] = when (value) {
            null -> null
            else -> AttributeValue.StringValue(value.get())
        }
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
                null -> null
                else -> Attribute(it.key, value)
            }
        }
}

