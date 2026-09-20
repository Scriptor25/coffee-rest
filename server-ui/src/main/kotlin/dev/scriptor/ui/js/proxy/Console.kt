package dev.scriptor.ui.js.proxy

import dev.scriptor.ui.js.JsCall
import dev.scriptor.ui.js.JsExpression
import dev.scriptor.ui.js.JsString
import dev.scriptor.ui.js.JsUndefined

class Console(value: JsExpression) : JsProxy(value) {

    val assert by proxy("assert")
    val clear by proxy("clear")
    val count by proxy("count")
    val countReset by proxy("countReset")
    val debug by proxy("debug")
    val dir by proxy("dir")
    val dirxml by proxy("dirxml")
    val error by proxy("error")
    val group by proxy("group")
    val groupCollapsed by proxy("groupCollapsed")
    val groupEnd by proxy("groupEnd")
    val info by proxy("info")
    val log by proxy("log")
    val profile by proxy("profile")
    val profileEnd by proxy("profileEnd")
    val table by proxy("table")
    val time by proxy("time")
    val timeEnd by proxy("timeEnd")
    val timeLog by proxy("timeLog")
    val timeStamp by proxy("timeStamp")
    val trace by proxy("trace")
    val warn by proxy("warn")

    fun assert(assertion: JsExpression, vararg values: JsExpression): JsCall {
        return (assert)(assertion, *values)
    }

    fun assert(assertion: JsExpression, message: String, vararg substitutes: JsExpression): JsCall {
        return (assert)(assertion, JsString(message), *substitutes)
    }

    fun clear(): JsCall {
        return (clear)()
    }

    fun count(label: String): JsCall {
        return (count)(JsString(label))
    }

    fun count(label: JsExpression = JsUndefined): JsCall {
        return (count)(label)
    }

    fun countReset(label: String): JsCall {
        return (countReset)(JsString(label))
    }

    fun countReset(label: JsExpression = JsUndefined): JsCall {
        return (countReset)(label)
    }

    fun debug(vararg values: JsExpression): JsCall {
        return (debug)(*values)
    }

    fun debug(message: String, vararg substitutes: JsExpression): JsCall {
        return (debug)(JsString(message), *substitutes)
    }

    fun dir(value: JsExpression, options: JsExpression = JsUndefined): JsCall {
        return (dir)(value, options)
    }

    fun dirxml(value: JsExpression): JsCall {
        return (dirxml)(value)
    }

    fun error(vararg values: JsExpression): JsCall {
        return (error)(*values)
    }

    fun error(message: String, vararg substitutes: JsExpression): JsCall {
        return (error)(JsString(message), *substitutes)
    }

    fun group(label: String): JsCall {
        return (group)(JsString(label))
    }

    fun group(label: JsExpression = JsUndefined): JsCall {
        return (group)(label)
    }

    fun groupCollapsed(label: String): JsCall {
        return (groupCollapsed)(JsString(label))
    }

    fun groupCollapsed(label: JsExpression = JsUndefined): JsCall {
        return (groupCollapsed)(label)
    }

    fun groupEnd(): JsCall {
        return (groupEnd)()
    }

    fun info(vararg values: JsExpression): JsCall {
        return (info)(*values)
    }

    fun info(message: String, vararg substitutes: JsExpression): JsCall {
        return (info)(JsString(message), *substitutes)
    }

    fun log(vararg values: JsExpression): JsCall {
        return (log)(*values)
    }

    fun log(message: String, vararg substitutes: JsExpression): JsCall {
        return (log)(JsString(message), *substitutes)
    }

    fun profile(name: String): JsCall {
        return (profile)(JsString(name))
    }

    fun profile(name: JsExpression): JsCall {
        return (profile)(name)
    }

    fun profileEnd(name: String): JsCall {
        return (profileEnd)(JsString(name))
    }

    fun profileEnd(name: JsExpression): JsCall {
        return (profileEnd)(name)
    }

    fun table(data: JsExpression, columns: JsExpression = JsUndefined): JsCall {
        return (table)(data, columns)
    }

    fun time(label: String): JsCall {
        return (time)(JsString(label))
    }

    fun time(label: JsExpression = JsUndefined): JsCall {
        return (time)(label)
    }

    fun timeEnd(label: String): JsCall {
        return (timeEnd)(JsString(label))
    }

    fun timeEnd(label: JsExpression = JsUndefined): JsCall {
        return (timeEnd)(label)
    }

    fun timeLog(label: String, vararg values: JsExpression): JsCall {
        return (timeLog)(JsString(label), *values)
    }

    fun timeLog(label: JsExpression = JsUndefined, vararg values: JsExpression): JsCall {
        return (timeLog)(label, *values)
    }

    fun timeStamp(label: String): JsCall {
        return (timeStamp)(JsString(label))
    }

    fun timeStamp(label: JsExpression = JsUndefined): JsCall {
        return (timeStamp)(label)
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
        return (timeStamp)(
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
        return (trace)(*values)
    }

    fun warn(vararg values: JsExpression): JsCall {
        return (warn)(*values)
    }

    fun warn(message: String, vararg substitutes: JsExpression): JsCall {
        return (warn)(JsString(message), *substitutes)
    }
}
