package dev.scriptor.sys

import kotlin.time.Duration
import kotlin.time.toJavaDuration

internal fun wrapThread(instance: java.lang.Thread): Thread {
    return object : Thread {
        override val id: Long
            get() = instance.threadId()
        override val alive: Boolean
            get() = instance.isAlive
        override val interrupted: Boolean
            get() = instance.isInterrupted
        override var name: String?
            get() = instance.name
            set(value) {
                instance.name = value
            }
        override var daemon: Boolean
            get() = instance.isDaemon
            set(value) {
                instance.isDaemon = value
            }
        override var priority: Int
            get() = instance.priority
            set(value) {
                instance.priority = value
            }

        override fun interrupt() {
            instance.interrupt()
        }

        override fun join(timeout: Duration) {
            instance.join(timeout.toJavaDuration())
        }
    }
}

internal actual fun createThread(block: () -> Unit): Thread {
    val instance = java.lang.Thread(block)

    return wrapThread(instance)
}

internal actual fun currentThread(): Thread {
    val instance = java.lang.Thread.currentThread()

    return wrapThread(instance)
}
