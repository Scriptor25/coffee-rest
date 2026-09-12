package dev.scriptor.server

import dev.scriptor.reflect.Type
import dev.scriptor.reflect.getClass
import dev.scriptor.reflect.getType
import dev.scriptor.reflect.isAssignable
import dev.scriptor.server.converter.AnyConverterFn
import dev.scriptor.server.converter.Conversion
import dev.scriptor.server.converter.ConversionStep
import dev.scriptor.server.converter.ConverterFn
import kotlin.reflect.KClass
import kotlin.reflect.KType

class Provider {

    private val converters = mutableMapOf<Pair<Type, Type>, AnyConverterFn>()
    private val conversions = mutableMapOf<Pair<Type, Type>, Conversion>()
    private val contexts = mutableMapOf<Type, Any>()
    private val named = mutableMapOf<String, Any?>()

    private fun hasConversion(key: Pair<Type, Type>): Boolean {
        return getConversion(key) != null
    }

    private fun getConversion(key: Pair<Type, Type>): AnyConverterFn? {
        if (key in conversions) {
            val convert = conversions[key]!!
            return { convert(it) }
        }

        data class Node(
            val type: Type,
            val path: List<ConversionStep>,
        )

        val queue = ArrayDeque<Node>()
        val visited = mutableSetOf<Type>()

        queue.add(Node(key.first, emptyList()))

        while (queue.isNotEmpty()) {

            val current = queue.removeFirst()

            if (!visited.add(current.type)) continue

            if (isAssignable(key.second, current.type)) {
                val convert = Conversion(current.path)
                conversions[key] = convert
                return { convert(it) }
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

    operator fun set(key: Pair<Type, Type>, value: AnyConverterFn) {
        converters[key] = value
    }

    operator fun set(key: Type, value: Any) {
        contexts[key] = value
    }

    operator fun set(key: KType, value: Any) {
        val type = getType(key)

        set(type, value)
    }

    operator fun set(key: KClass<*>, value: Any) {
        val type = getClass(key).createType()

        set(type, value)
    }

    operator fun set(key: String, value: Any?) {
        named[key] = value
    }

    operator fun contains(key: Pair<Type, Type>): Boolean {
        return hasConversion(key)
    }

    operator fun contains(key: Type): Boolean {
        return contexts.any { isAssignable(key, it.key) }
    }

    operator fun contains(key: String): Boolean {
        return key in named
    }

    operator fun get(key: Pair<Type, Type>): AnyConverterFn? {
        return getConversion(key)
    }

    operator fun get(key: Type): Any? {
        return contexts
            .filter { isAssignable(key, it.key) }
            .values
            .firstOrNull()
    }

    operator fun get(key: KType): Any? {
        val key = getType(key)

        return get(key)
    }

    operator fun get(key: String): Any? {
        return named[key]
    }

    inline fun <reified S, reified D> setT(noinline value: ConverterFn<S, D>) {
        val src = getType<S>()
        val dst = getType<D>()

        set(src to dst, value as AnyConverterFn)
    }

    inline fun <reified T : Any> setT(value: T) {
        val type = getType<T>()

        set(type, value)
    }

    inline fun <reified T> containsT(): Boolean {
        val type = getType<T>()

        return contains(type)
    }

    inline fun <reified T> getT(): T? {
        val key = getType<T>()

        return get(key) as? T
    }

    inline fun <reified T> getT(key: String): T? {
        return get(key) as? T
    }

    inline operator fun <reified S, reified D> invoke(value: S): D {
        val src = getType<S>()
        val dst = getType<D>()

        val convert = get(src to dst)
            ?: error("no conversion path from $src to $dst")

        return convert(value) as D
    }
}
