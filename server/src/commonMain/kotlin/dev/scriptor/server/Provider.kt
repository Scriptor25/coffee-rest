package dev.scriptor.server

import dev.scriptor.reflect.Type
import dev.scriptor.reflect.getType
import dev.scriptor.reflect.isAssignable
import dev.scriptor.server.converter.Conversion
import dev.scriptor.server.converter.ConversionStep

class Provider {

    private val converters = mutableMapOf<Pair<Type, Type>, (Any?) -> Any?>()
    private val conversions = mutableMapOf<Pair<Type, Type>, Conversion<Any?, Any?>>()
    private val contexts = mutableMapOf<Type, Any?>()
    private val named = mutableMapOf<String, Any?>()

    operator fun contains(key: Pair<Type, Type>): Boolean {
        return get(key) != null
    }

    operator fun get(key: Pair<Type, Type>): Conversion<Any?, Any?>? {
        if (key in conversions) {
            return conversions[key]
        }

        data class Node(
            val type: Type,
            val path: List<ConversionStep>,
        )

        val queue = mutableListOf<Node>()
        val visited = mutableSetOf<Type>()

        queue.add(Node(key.first, emptyList()))

        while (queue.isNotEmpty()) {

            val current = queue.removeFirst()

            if (!visited.add(current.type)) continue

            if (isAssignable(key.second, current.type)) {
                val path = Conversion<Any?, Any?>(current.path)
                conversions[key] = path
                return path
            }

            val edges = converters.filter { isAssignable(it.key.first, current.type) }

            for ((key, value) in edges) {
                queue += Node(
                    key.second,
                    current.path + ConversionStep(
                        current.type,
                        key.second,
                        value,
                    )
                )
            }
        }

        return null
    }

    operator fun set(key: Pair<Type, Type>, converter: (Any?) -> Any?) {
        converters[key] = converter
    }

    operator fun contains(key: Type): Boolean {
        return contexts.any { isAssignable(key, it.key) }
    }

    operator fun get(key: Type): Any? {
        return contexts.entries.find { isAssignable(key, it.key) }
    }

    operator fun set(key: Type, value: Any?) {
        contexts[key] = value
    }

    operator fun contains(name: String): Boolean {
        return name in named
    }

    operator fun get(name: String): Any? {
        return named[name]
    }

    operator fun set(name: String, value: Any?) {
        named[name] = value
    }

    inline operator fun <reified S, reified D> invoke(crossinline converter: (S) -> D) {
        set(getType<S>() to getType<D>()) { converter(it as S) }
    }

    inline fun <reified T> register(value: T) {
        set(getType<T>(), value)
    }

    inline operator fun <reified T> get(name: String): T {
        return get(name) as T
    }

    inline fun <reified S, reified D> converter(): Conversion<S, D>? {
        val src = getType<S>()
        val dst = getType<D>()

        return this[src to dst] as? Conversion<S, D>
    }

    inline fun <reified S, reified D> convert(value: S): D {
        val src = getType<S>()
        val dst = getType<D>()

        val path = this[src to dst] as? Conversion<S, D>
            ?: error("unsupported conversion from $src to $dst")

        return path(value)
    }
}
