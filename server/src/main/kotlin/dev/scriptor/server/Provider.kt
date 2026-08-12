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

    fun register(src: Type, dst: Type, converter: AnyConverterFn) {
        converters[src to dst] = converter
    }

    fun register(type: Type, value: Any) {
        contexts[type] = value
    }

    fun register(name: String, value: Any?) {
        named[name] = value
    }

    fun hasConversion(src: Type, dst: Type): Boolean {
        return getConversion(src, dst) != null
    }

    fun getConversion(src: Type, dst: Type): AnyConverterFn? {
        val key = src to dst

        if (key in conversions) {
            val convert = conversions[key]!!
            return { convert(it) }
        }

        data class Node(
            val type: Type,
            val path: List<ConversionStep>,
        )

        val queue = ArrayDeque<Node>()
        val visited = HashSet<Type>()

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

    fun hasContext(type: Type): Boolean {
        return contexts.any { isAssignable(type, it.key) }
    }

    fun getContext(type: Type): Any? {
        return contexts
            .filter { isAssignable(type, it.key) }
            .values
            .firstOrNull()
    }

    fun hasNamed(name: String): Boolean {
        return name in named
    }

    fun getNamed(name: String): Any? {
        return named[name]
    }

    operator fun set(key: Pair<Type, Type>, value: AnyConverterFn) {
        register(key.first, key.second, value)
    }

    operator fun set(key: Type, value: Any) {
        register(key, value)
    }

    operator fun set(key: KType, value: Any) {
        val type = getType(key)

        register(type, value)
    }

    operator fun set(key: KClass<*>, value: Any) {
        val type = getClass(key).createType()

        register(type, value)
    }

    operator fun set(key: String, value: Any?) {
        register(key, value)
    }

    operator fun contains(key: Pair<Type, Type>): Boolean {
        return hasConversion(key.first, key.second)
    }

    operator fun contains(key: Type): Boolean {
        return hasContext(key)
    }

    operator fun contains(key: String): Boolean {
        return hasNamed(key)
    }

    operator fun get(key: Pair<Type, Type>): AnyConverterFn? {
        return getConversion(key.first, key.second)
    }

    operator fun get(key: Type): Any? {
        return getContext(key)
    }

    operator fun get(key: KType): Any? {
        val type = getType(key)

        return getContext(type)
    }

    operator fun get(key: String): Any? {
        return getNamed(key)
    }

    inline fun <reified S, reified D> registerT(noinline value: ConverterFn<S, D>) {
        val src = getType<S>()
        val dst = getType<D>()

        register(src, dst, value as AnyConverterFn)
    }

    inline fun <reified T : Any> registerT(value: T) {
        val type = getType<T>()

        register(type, value)
    }

    inline fun <reified S, reified D> hasConversionT(): Boolean {
        val src = getType<S>()
        val dst = getType<D>()

        return hasConversion(src, dst)
    }

    inline fun <reified T> hasContextT(): Boolean {
        val type = getType<T>()

        return hasContext(type)
    }

    inline fun <reified S, reified D> getConversionT(): ConverterFn<S, D>? {
        val src = getType<S>()
        val dst = getType<D>()

        val convert = getConversion(src, dst) ?: return null

        return { convert(it) as D }
    }

    inline fun <reified T> getContextT(): T? {
        val type = getType<T>()

        return getContext(type) as? T
    }

    inline fun <reified T> getNamedT(key: String): T? {
        return getNamed(key) as? T
    }

    inline operator fun <reified S, reified D> invoke(value: S): D {
        val src = getType<S>()
        val dst = getType<D>()

        val convert = getConversion(src, dst) ?: error("no conversion path from $src to $dst")

        return convert(value) as D
    }
}
