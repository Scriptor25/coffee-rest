package dev.scriptor.server.http

import java.nio.file.Path

class Pathname(path: Path) : Comparable<Pathname> {

    data class Parameter(val index: Int, val collecting: Boolean)

    private val path = path.toAbsolutePath().normalize()

    private val priority: Int
    private val index: Int

    private val regex: Regex
    private val parameters = mutableMapOf<String, Parameter>()

    private val input = """([^\[]+)|\[([^]]*)]""".toRegex()

    init {
        val route = StringBuilder().append("^")

        var segmentCount = 0

        var staticChars = 0
        var staticCount = 0
        var staticFirst = Int.MAX_VALUE

        var parameterCount = 0
        var collectingCount = 0

        val pathname = this.path.toString().lowercase()

        for (match in input.findAll(pathname)) {
            val group1 = match.groups[1]
            val group2 = match.groups[2]

            when {
                group1 != null -> {
                    val value = group1.value

                    route.append(Regex.escape(value))

                    value
                        .split("/+".toRegex())
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .forEach {
                            staticChars += it.length
                            staticCount++

                            if (staticFirst == Int.MAX_VALUE) {
                                staticFirst = segmentCount
                            }

                            segmentCount++
                        }
                }

                group2 != null -> {
                    val name = group2.value

                    val collecting: Boolean
                    if (name.isNotEmpty()) {
                        collecting = name.endsWith("+")

                        val name =
                            if (collecting) name.substring(0, name.length - 1)
                            else name

                        parameters[name.lowercase()] = Parameter(parameterCount, collecting)
                    } else {
                        collecting = false
                    }

                    val last = match.range.endExclusive == pathname.length

                    route.append(if (collecting) if (last) "(.*)" else "(.+)" else "([^\\/]+)")

                    if (collecting) {
                        collectingCount++
                    }

                    parameterCount++
                    segmentCount++
                }
            }
        }

        route.append("$")

        regex = route.toString().toRegex(RegexOption.IGNORE_CASE)

        var score = 0

        score += staticCount * 100
        score -= collectingCount * 50
        score += staticChars
        score += segmentCount * 5

        priority = score
        index = staticFirst
    }

    operator fun contains(path: String): Boolean {
        return regex.matches(path)
    }

    operator fun get(path: String, name: String): Any? {
        val parameter = parameters[name] ?: return null
        val match = regex.matchEntire(path) ?: return null
        val value = match.groupValues[parameter.index + 1]

        if (!parameter.collecting) {
            return value
        }

        return value.split("/+".toRegex()).toTypedArray()
    }

    override fun compareTo(other: Pathname): Int {
        if (this.priority != other.priority) {
            return this.priority.compareTo(other.priority)
        }
        return other.index.compareTo(this.index)
    }

    override fun toString(): String {
        return path.toString()
    }
}
