package dev.scriptor.server.util

interface Log {

    enum class Level {
        ALL,
        CONFIG,
        INFO,
        WARN,
        ERROR,
    }

    companion object {
        fun create(name: String, level: Level): Log {
            return object : Log {
                override val name: String = name
                override val level: Level = level

                override fun log(level: Level, message: String) {
                    if (level < this.level) return
                    println("[$name][$level] $message")
                }
            }
        }
    }

    val name: String
    val level: Level

    fun log(level: Level, message: String)

    fun config(message: String) = log(Level.CONFIG, message)
    fun info(message: String) = log(Level.INFO, message)
    fun warn(message: String) = log(Level.WARN, message)
    fun error(message: String) = log(Level.ERROR, message)
}
