package dev.scriptor.ui.js.builder

import dev.scriptor.ui.js.*

data class JsConsoleDirOptions(
    val colors: JsExpression = JsUndefined,
    val depth: JsExpression = JsUndefined,
    val showHidden: JsExpression = JsUndefined,
)

enum class JsConsoleTimeStampColor(val value: String) {
    PRIMARY("primary"),
    PRIMARY_LIGHT("primary-light"),
    PRIMARY_DARK("primary-dark"),
    SECONDARY("secondary"),
    SECONDARY_LIGHT("secondary-light"),
    SECONDARY_DARK("secondary-dark"),
    TERTIARY("tertiary"),
    TERTIARY_LIGHT("tertiary-light"),
    TERTIARY_DARK("tertiary-dark"),
    ERROR("error"),

}

class JsConsoleBuilder<T>(
    val builder: JsBuilder<T>,
) {

    private fun call(name: String, vararg arguments: JsExpression): JsCall {
        val console = JsSymbol("console")
        val member = JsMember(console, name)
        return builder.call(member, *arguments)
    }

    fun assert(assertion: JsExpression, vararg values: JsExpression): JsCall {
        return call("assert", assertion, *values)
    }

    fun assert(assertion: JsExpression, message: String, vararg substitutes: JsExpression): JsCall {
        return call("assert", assertion, JsString(message), *substitutes)
    }

    fun assert(assertion: JsExpression, message: JsExpression, vararg substitutes: JsExpression): JsCall {
        return call("assert", assertion, message, *substitutes)
    }

    fun clear(): JsCall {
        return call("clear")
    }

    fun count(): JsCall {
        return call("count")
    }

    fun count(label: String): JsCall {
        return call("count", JsString(label))
    }

    fun count(label: JsExpression): JsCall {
        return call("count", label)
    }

    fun countReset(): JsCall {
        return call("countReset")
    }

    fun countReset(label: String): JsCall {
        return call("countReset", JsString(label))
    }

    fun countReset(label: JsExpression): JsCall {
        return call("countReset", label)
    }

    fun debug(vararg values: JsExpression): JsCall {
        return call("debug", *values)
    }

    fun debug(message: String, vararg substitutes: JsExpression): JsCall {
        return call("debug", JsString(message), *substitutes)
    }

    fun debug(message: JsExpression, vararg substitutes: JsExpression): JsCall {
        return call("debug", message, *substitutes)
    }

    fun dir(value: JsExpression): JsCall {
        return call("dir", value)
    }

    fun dir(value: JsExpression, options: JsConsoleDirOptions): JsCall {
        return call(
            "dir", value, JsObject(
                "colors" to options.colors,
                "depth" to options.depth,
                "showHidden" to options.showHidden,
            )
        )
    }

    fun dirxml(value: JsExpression): JsCall {
        return call("dirxml", value)
    }

    fun error(vararg values: JsExpression): JsCall {
        return call("error", *values)
    }

    fun error(message: String, vararg substitutes: JsExpression): JsCall {
        return call("error", JsString(message), *substitutes)
    }

    fun error(message: JsExpression, vararg substitutes: JsExpression): JsCall {
        return call("error", message, *substitutes)
    }

    fun group(): JsCall {
        return call("group")
    }

    fun group(label: String): JsCall {
        return call("group", JsString(label))
    }

    fun group(label: JsExpression): JsCall {
        return call("group", label)
    }

    fun groupCollapsed(): JsCall {
        return call("groupCollapsed")
    }

    fun groupCollapsed(label: String): JsCall {
        return call("groupCollapsed", JsString(label))
    }

    fun groupCollapsed(label: JsExpression): JsCall {
        return call("groupCollapsed", label)
    }

    fun groupEnd(): JsCall {
        return call("groupEnd")
    }

    fun info(vararg values: JsExpression): JsCall {
        return call("info", *values)
    }

    fun info(message: String, vararg substitutes: JsExpression): JsCall {
        return call("info", JsString(message), *substitutes)
    }

    fun info(message: JsExpression, vararg substitutes: JsExpression): JsCall {
        return call("info", message, *substitutes)
    }

    fun log(vararg values: JsExpression): JsCall {
        return call("log", *values)
    }

    fun log(message: String, vararg substitutes: JsExpression): JsCall {
        return call("log", JsString(message), *substitutes)
    }

    fun log(message: JsExpression, vararg substitutes: JsExpression): JsCall {
        return call("log", message, *substitutes)
    }

    fun profile(name: String): JsCall {
        return call("profile", JsString(name))
    }

    fun profile(name: JsExpression): JsCall {
        return call("profile", name)
    }

    fun profileEnd(name: String): JsCall {
        return call("profileEnd", JsString(name))
    }

    fun profileEnd(name: JsExpression): JsCall {
        return call("profileEnd", name)
    }

    fun table(data: JsExpression): JsCall {
        return call("table", data)
    }

    fun table(data: JsExpression, columns: JsExpression): JsCall {
        return call("table", data, columns)
    }

    fun time(): JsCall {
        return call("time")
    }

    fun time(label: String): JsCall {
        return call("time", JsString(label))
    }

    fun time(label: JsExpression): JsCall {
        return call("time", label)
    }

    fun timeEnd(): JsCall {
        return call("timeEnd")
    }

    fun timeEnd(label: String): JsCall {
        return call("timeEnd", JsString(label))
    }

    fun timeEnd(label: JsExpression): JsCall {
        return call("timeEnd", label)
    }

    fun timeLog(): JsCall {
        return call("timeLog")
    }

    fun timeLog(label: String): JsCall {
        return call("timeLog", JsString(label))
    }

    fun timeLog(label: JsExpression): JsCall {
        return call("timeLog", label)
    }

    fun timeLog(label: String, vararg values: JsExpression): JsCall {
        return call("timeLog", JsString(label), *values)
    }

    fun timeLog(label: JsExpression, vararg values: JsExpression): JsCall {
        return call("timeLog", label, *values)
    }

    fun timeStamp(): JsCall {
        return call("timeStamp")
    }

    fun timeStamp(label: String): JsCall {
        return call("timeStamp", JsString(label))
    }

    fun timeStamp(label: JsExpression): JsCall {
        return call("timeStamp", label)
    }

    fun timeStamp(
        label: String,
        start: JsExpression = JsUndefined,
        end: JsExpression = JsUndefined,
        trackName: String? = null,
        trackGroup: String? = null,
        color: JsConsoleTimeStampColor? = null,
        data: JsExpression = JsUndefined,
    ): JsCall {
        return call(
            "timeStamp",
            JsString(label),
            start,
            end,
            trackName?.let { JsString(it) } ?: JsUndefined,
            trackGroup?.let { JsString(it) } ?: JsUndefined,
            color?.let { JsString(it.value) } ?: JsUndefined,
            data,
        )
    }

    fun timeStamp(
        label: JsExpression,
        start: JsExpression = JsUndefined,
        end: JsExpression = JsUndefined,
        trackName: JsExpression = JsUndefined,
        trackGroup: JsExpression = JsUndefined,
        color: JsExpression = JsUndefined,
        data: JsExpression = JsUndefined,
    ): JsCall {
        return call(
            "timeStamp",
            label,
            start,
            end,
            trackName,
            trackGroup,
            color,
            data,
        )
    }

    fun trace(vararg values: JsExpression): JsCall {
        return call("trace", *values)
    }

    fun warn(vararg values: JsExpression): JsCall {
        return call("warn", *values)
    }

    fun warn(message: String, vararg substitutes: JsExpression): JsCall {
        return call("warn", JsString(message), *substitutes)
    }

    fun warn(message: JsExpression, vararg substitutes: JsExpression): JsCall {
        return call("warn", message, *substitutes)
    }
}
