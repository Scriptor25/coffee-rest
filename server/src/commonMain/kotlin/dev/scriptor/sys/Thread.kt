package dev.scriptor.sys

import dev.scriptor.time.Duration

interface Thread {

    companion object {
        fun create(name: String? = null, block: () -> Unit): Thread {
            TODO()
        }

        val current: Thread
            get() = TODO()
    }

    val id: Long
    val alive: Boolean
    val interrupted: Boolean

    var name: String?
    var daemon: Boolean
    var priority: Int

    fun interrupt()
    fun join(timeout: Duration)
}
