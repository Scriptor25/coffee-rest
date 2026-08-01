package dev.scriptor.server

import dev.scriptor.server.converter.ConversionPath
import dev.scriptor.server.converter.ConversionStep
import dev.scriptor.server.converter.Converter
import dev.scriptor.server.type.isAssignable
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KType

class Provider {

    private val conversionSteps = mutableListOf<ConversionStep>()
    private val conversionPaths = mutableMapOf<Pair<KType, KType>, ConversionPath<Any, Any>>()
    private val contexts = mutableMapOf<KClass<*>, Any>()
    private val named = mutableMapOf<String, Any?>()

    operator fun set(key: Pair<KType, KType>, converter: Converter<Any, Any>) {
        conversionSteps += ConversionStep(key.first, key.second, converter)
    }

    operator fun get(key: Pair<KType, KType>): ConversionPath<Any, Any>? {
        if (key in conversionPaths) {
            return conversionPaths[key]
        }

        data class Node(
            val type: KType,
            val path: List<ConversionStep>,
        )

        val queue = ArrayDeque<Node>()
        val visited = HashSet<KType>()

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

    operator fun contains(key: Pair<KType, KType>): Boolean = get(key) != null

    operator fun plusAssign(value: Any) {
        contexts[value::class] = value
    }

    operator fun get(klass: KClass<*>): Any? = contexts[klass]

    operator fun contains(klass: KClass<*>): Boolean = klass in contexts

    operator fun <T> set(name: String, value: T) {
        named[name] = value
    }

    operator fun <T> get(name: String): T = named[name] as T

    operator fun contains(name: String): Boolean = name in named
}
