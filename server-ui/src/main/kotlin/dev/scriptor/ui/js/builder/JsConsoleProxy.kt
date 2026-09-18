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

data object JsConsoleProxy : JsSymbol("console") {

    fun assert(assertion: JsExpression, vararg values: JsExpression): JsCall {
        return this["assert"](assertion, *values)
    }

    fun assert(assertion: JsExpression, message: String, vararg substitutes: JsExpression): JsCall {
        return assert(assertion, JsString(message), *substitutes)
    }

    fun clear(): JsCall {
        return this["clear"]()
    }

    fun count(label: String): JsCall {
        return count(JsString(label))
    }

    fun count(label: JsExpression = JsUndefined): JsCall {
        return this["count"](label)
    }

    fun countReset(label: String): JsCall {
        return countReset(JsString(label))
    }

    fun countReset(label: JsExpression = JsUndefined): JsCall {
        return this["countReset"](label)
    }

    fun debug(vararg values: JsExpression): JsCall {
        return this["debug"](*values)
    }

    fun debug(message: String, vararg substitutes: JsExpression): JsCall {
        return debug(JsString(message), *substitutes)
    }

    fun dir(value: JsExpression, options: JsConsoleDirOptions): JsCall {
        return dir(
            value,
            JsObject(
                "colors" to options.colors,
                "depth" to options.depth,
                "showHidden" to options.showHidden,
            ),
        )
    }

    fun dir(value: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return this["dir"](value, options)
    }

    fun dirxml(value: JsExpression): JsCall {
        return this["dirxml"](value)
    }

    fun error(vararg values: JsExpression): JsCall {
        return this["error"](*values)
    }

    fun error(message: String, vararg substitutes: JsExpression): JsCall {
        return error(JsString(message), *substitutes)
    }

    fun group(label: String): JsCall {
        return group(JsString(label))
    }

    fun group(label: JsExpression = JsUndefined): JsCall {
        return this["group"](label)
    }

    fun groupCollapsed(label: String): JsCall {
        return groupCollapsed(JsString(label))
    }

    fun groupCollapsed(label: JsExpression = JsUndefined): JsCall {
        return this["groupCollapsed"](label)
    }

    fun groupEnd(): JsCall {
        return this["groupEnd"]()
    }

    fun info(vararg values: JsExpression): JsCall {
        return this["info"](*values)
    }

    fun info(message: String, vararg substitutes: JsExpression): JsCall {
        return info(JsString(message), *substitutes)
    }

    fun log(vararg values: JsExpression): JsCall {
        return this["log"](*values)
    }

    fun log(message: String, vararg substitutes: JsExpression): JsCall {
        return log(JsString(message), *substitutes)
    }

    fun profile(name: String): JsCall {
        return profile(JsString(name))
    }

    fun profile(name: JsExpression): JsCall {
        return this["profile"](name)
    }

    fun profileEnd(name: String): JsCall {
        return profileEnd(JsString(name))
    }

    fun profileEnd(name: JsExpression): JsCall {
        return this["profileEnd"](name)
    }

    fun table(data: JsExpression, columns: JsExpression = JsUndefined): JsCall {
        return this["table"](data, columns)
    }

    fun time(label: String): JsCall {
        return time(JsString(label))
    }

    fun time(label: JsExpression = JsUndefined): JsCall {
        return this["time"](label)
    }

    fun timeEnd(label: String): JsCall {
        return timeEnd(JsString(label))
    }

    fun timeEnd(label: JsExpression = JsUndefined): JsCall {
        return this["timeEnd"](label)
    }

    fun timeLog(label: String, vararg values: JsExpression): JsCall {
        return timeLog(JsString(label), *values)
    }

    fun timeLog(label: JsExpression = JsUndefined, vararg values: JsExpression): JsCall {
        return this["timeLog"](label, *values)
    }

    fun timeStamp(label: String): JsCall {
        return timeStamp(JsString(label))
    }

    fun timeStamp(label: JsExpression = JsUndefined): JsCall {
        return this["timeStamp"](label)
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
        return timeStamp(
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
        return this["timeStamp"](
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
        return this["trace"](*values)
    }

    fun warn(vararg values: JsExpression): JsCall {
        return this["warn"](*values)
    }

    fun warn(message: String, vararg substitutes: JsExpression): JsCall {
        return warn(JsString(message), *substitutes)
    }
}
