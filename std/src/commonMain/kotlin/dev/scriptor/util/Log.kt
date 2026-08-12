package dev.scriptor.util

import dev.scriptor.util.Log.Level
import kotlin.time.Clock

interface Log {

    enum class Level {
        ALL,
        CONFIG,
        INFO,
        WARN,
        ERROR,
    }

    val name: String
    val level: Level

    fun log(level: Level, message: String)

    fun config(message: String) = log(Level.CONFIG, message)
    fun info(message: String) = log(Level.INFO, message)
    fun warn(message: String) = log(Level.WARN, message)
    fun error(message: String) = log(Level.ERROR, message)
}

fun Log(name: String, level: Level = Level.ALL): Log {
    return object : Log {
        override val name: String = name
        override val level: Level = level

        override fun log(level: Level, message: String) {
            if (level < this.level) return

            val instant = Clock.System.now()
            println("[$name][$level][$instant] $message")
        }
    }
}
