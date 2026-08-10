package dev.scriptor.sys

import kotlin.time.Duration

interface Thread {

    companion object {
        val current: Thread
            get() = currentThread()
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

fun Thread(block: () -> Unit): Thread {
    return createThread(block)
}

internal expect fun createThread(block: () -> Unit): Thread
internal expect fun currentThread(): Thread
