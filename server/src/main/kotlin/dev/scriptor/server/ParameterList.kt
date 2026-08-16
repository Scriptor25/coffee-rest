package dev.scriptor.server

class ParameterList {

    private val map: MutableMap<String, MutableList<String>>

    constructor() {
        this.map = mutableMapOf()
    }

    constructor(values: Map<String, List<String>>) {
        this.map = values
            .mapKeys { it.key.lowercase() }
            .mapValues { it.value.toMutableList() }
            .toMutableMap()
    }

    constructor(pairs: List<Pair<String, String>>) {
        this.map = mutableMapOf()
        for ((key, value) in pairs) {
            this.map.computeIfAbsent(key.lowercase()) { mutableListOf() }.add(value)
        }
    }

    constructor(vararg pairs: Pair<String, String>) {
        this.map = mutableMapOf()
        for ((key, value) in pairs) {
            this.map.computeIfAbsent(key.lowercase()) { mutableListOf() }.add(value)
        }
    }

    constructor(other: ParameterList) {
        this.map = other.map.toMutableMap()
    }

    operator fun set(key: String, value: String) {
        map[key.lowercase()] = mutableListOf(value)
    }

    operator fun get(key: String): String? = map[key.lowercase()].orEmpty().firstOrNull()

    operator fun contains(key: String): Boolean = map[key.lowercase()].orEmpty().isNotEmpty()

    fun append(key: String, value: String) {
        map.computeIfAbsent(key.lowercase()) { mutableListOf() }.add(value)
    }

    fun getAll(key: String): List<String> = map[key.lowercase()].orEmpty()

    operator fun iterator() = map.iterator()
}
