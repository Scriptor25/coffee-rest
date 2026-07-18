package dev.scriptor.server.http

import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern

class HTTPRoute(path: Path) : Comparable<HTTPRoute> {

    data class Parameter(val index: Int, val collecting: Boolean)

    private val path: Path = path.toAbsolutePath()

    private val priority: Int
    private val index: Int

    private val pattern: Pattern
    private val parameters: MutableMap<String, Parameter> = HashMap()

    constructor(endpoint: String, resource: String) : this(Path.of(endpoint, resource))

    init {
        val pathname = this.path.toString().lowercase()

        val matcher = Pattern.compile("\\[(.*?)]").matcher(pathname)
        val route = StringBuilder().append("^")

        var end = 0

        var segmentCount = 0

        var staticChars = 0
        var staticCount = 0
        var staticFirst = Int.MAX_VALUE

        var parameterCount = 0
        var collectingCount = 0

        while (matcher.find()) {
            val staticPart = pathname.substring(end, matcher.start())

            if (staticPart.isNotEmpty()) {
                route.append(Pattern.quote(staticPart))

                val parts = staticPart.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (part in parts) {
                    if (part.isNotEmpty()) {
                        staticChars += part.length
                        staticCount++

                        if (staticFirst == Int.MAX_VALUE) {
                            staticFirst = segmentCount
                        }

                        segmentCount++
                    }
                }
            }

            val parameter = matcher.group(1).trim { it <= ' ' }

            val collecting = parameter.endsWith("+")
            val name = if (collecting) parameter.substring(0, parameter.length - 1) else parameter

            parameters[name] = Parameter(parameterCount, collecting)

            route.append(if (collecting) "(.+)" else "([^\\/]+)")

            if (collecting) {
                collectingCount++
            }

            parameterCount++
            segmentCount++

            end = matcher.end()
        }

        val tail = pathname.substring(end)
        route.append(Pattern.quote(tail))
        route.append("$")

        if (tail.isNotEmpty()) {
            val parts = tail.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (part in parts) {
                if (part.isNotEmpty()) {
                    staticChars += part.length
                    staticCount++

                    if (staticFirst == Int.MAX_VALUE) {
                        staticFirst = segmentCount
                    }

                    segmentCount++
                }
            }
        }

        pattern = Pattern.compile(route.toString())

        var score = 0

        score += staticCount * 100
        score -= collectingCount * 50
        score += staticChars
        score += segmentCount * 5

        priority = score
        index = staticFirst
    }

    fun matches(path: String): Boolean {
        return pattern.matcher(path.lowercase()).matches()
    }

    fun get(path: String, name: String): Any? {
        if (name !in parameters) {
            return null
        }

        val matcher = pattern.matcher(path)
        if (!matcher.matches()) {
            return null
        }

        val parameter = parameters[name]!!

        val value = matcher.group(parameter.index + 1)
        if (!parameter.collecting) {
            return value
        }

        return value.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    override fun compareTo(other: HTTPRoute): Int {
        if (this.priority != other.priority) {
            return this.priority.compareTo(other.priority)
        }
        return other.index.compareTo(this.index)
    }

    override fun toString(): String {
        return path.toString()
    }
}
