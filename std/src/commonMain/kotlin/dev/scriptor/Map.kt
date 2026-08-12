package dev.scriptor

fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, generate: (K) -> V): V {
    if (key in this) return this[key] as V
    val value = generate(key)
    this[key] = value
    return value
}
