package dev.scriptor.server.http

import dev.scriptor.stdlib.io.Path


class PathExpression(path: Path) : Comparable<PathExpression> {

    private data class Parameter(val index: Int, val collecting: Boolean)

    private val path: Path = path.toAbsolutePath()

    private val priority: Int
    private val index: Int

    private val expression: Regex
    private val parameters: MutableMap<String, Parameter> = HashMap()

    init {
        val pathname = this.path.toString().lowercase()

        val route = StringBuilder().append("^")

        var matches = """\[(.*?)]""".toRegex().findAll(pathname)
        var end = 0

        var segmentCount = 0

        var staticChars = 0
        var staticCount = 0
        var staticFirst = Int.MAX_VALUE

        var parameterCount = 0
        var collectingCount = 0

        for (match in matches) {
            val staticPart = pathname.substring(end, match.range.first)

            if (staticPart.isNotEmpty()) {
                route.append(Regex.escape(staticPart))

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

            val parameter = match.groupValues[1].trim { it <= ' ' }

            val collecting: Boolean
            if (parameter.isNotEmpty()) {
                collecting = parameter.endsWith("+")

                val name =
                    if (collecting) parameter.substring(0, parameter.length - 1)
                    else parameter

                parameters[name.lowercase()] = Parameter(parameterCount, collecting)
            } else {
                collecting = false
            }

            route.append(if (collecting) "(.*)" else "([^\\/]+)")

            if (collecting) {
                collectingCount++
            }

            parameterCount++
            segmentCount++

            end = match.range.last + 1
        }

        val tail = pathname.substring(end)
        if (tail.isNotEmpty()) route.append(Regex.escape(tail))
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

        expression = route.toString().toRegex()

        var score = 0

        score += staticCount * 100
        score -= collectingCount * 50
        score += staticChars
        score += segmentCount * 5

        priority = score
        index = staticFirst
    }

    fun matches(path: String): Boolean {
        return expression.matches(path.lowercase())
    }

    fun get(path: String, name: String): Any? {
        if (name !in parameters) {
            return null
        }

        val parameter = parameters[name]!!

        val match = expression.matchEntire(path) ?: return null
        val value = match.groupValues[parameter.index + 1]
        if (!parameter.collecting) {
            return value
        }

        return value.split("/").dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    override fun compareTo(other: PathExpression): Int {
        if (this.priority != other.priority) {
            return this.priority.compareTo(other.priority)
        }

        return other.index.compareTo(this.index)
    }

    override fun toString(): String {
        return path.toString()
    }
}
