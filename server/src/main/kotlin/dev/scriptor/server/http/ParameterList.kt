package dev.scriptor.server.http

class ParameterList {

    private val values: MutableMap<String, MutableList<String>>

    constructor() {
        this.values = HashMap()
    }

    constructor(values: Map<String, List<String>>) {
        this.values = values.mapValues { (key, value) -> value.toMutableList() }.toMutableMap()
    }

    operator fun set(key: String, value: String) {
        values[key] = mutableListOf(value)
    }

    operator fun get(key: String): String? {
        return values[key].orEmpty().firstOrNull()
    }

    operator fun contains(key: String): Boolean {
        return values[key].orEmpty().isNotEmpty()
    }

    fun append(key: String, value: String) {
        values.computeIfAbsent(key) { mutableListOf() }.add(value)
    }

    fun getAll(key: String): List<String> {
        return values[key].orEmpty()
    }
}
