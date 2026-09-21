package dev.scriptor.ui

import kotlin.reflect.KProperty

interface Delegate<T> {
    operator fun getValue(self: Any?, property: KProperty<*>): T
    operator fun setValue(self: Any?, property: KProperty<*>, value: T)
}
