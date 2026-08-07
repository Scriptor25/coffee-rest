package dev.scriptor.server

import dev.scriptor.server.converter.ConversionPath
import dev.scriptor.server.converter.ConversionStep
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.reflect.Type
import dev.scriptor.server.type.isAssignable
import java.util.*
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.typeOf

class Provider {

    private val conversionSteps = mutableListOf<ConversionStep>()
    private val conversionPaths = mutableMapOf<Pair<Type, Type>, ConversionPath<Any, Any>>()
    private val contexts = mutableListOf<Any>()
    private val named = mutableMapOf<String, Any?>()

    operator fun set(key: Pair<Type, Type>, converter: Converter<Any?, Any?>) {
        conversionSteps += ConversionStep(key.first, key.second, converter)
    }

    operator fun get(key: Pair<Type, Type>): ConversionPath<Any, Any>? {
        if (key in conversionPaths) {
            return conversionPaths[key]
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
                val path = ConversionPath<Any, Any>(current.path)
                conversionPaths[key] = path
                return path
            }

            val edges = conversionSteps.filter { isAssignable(it.src, current.type) }

            for ((_, next, converter) in edges) {
                queue += Node(
                    next,
                    current.path + ConversionStep(
                        current.type,
                        next,
                        converter,
                    )
                )
            }
        }

        return null
    }

    operator fun contains(key: Pair<Type, Type>): Boolean = get(key) != null

    operator fun plusAssign(value: Any) {
        contexts += value
    }

    operator fun get(type: Type): Any? = contexts.find { isAssignable(type, it::class.starProjectedType) }

    operator fun contains(type: Type): Boolean = contexts.any { isAssignable(type, it::class.starProjectedType) }

    operator fun <T> set(name: String, value: T) {
        named[name] = value
    }

    operator fun <T> get(name: String): T = named[name] as T

    operator fun contains(name: String): Boolean = name in named
}

inline fun <reified S : Any, reified D : Any> Provider.convert(): ConversionPath<S, D>? {
    val src = typeOf<S>()
    val dst = typeOf<D>()

    return this[src to dst] as? ConversionPath<S, D>
}
