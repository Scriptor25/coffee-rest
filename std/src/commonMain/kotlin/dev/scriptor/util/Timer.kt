package dev.scriptor.util

import kotlin.time.Duration
import kotlin.time.Instant

interface Timer {

    fun cancel()

    fun purge(): Int

    fun schedule(task: TimerTask, instant: Instant, period: Duration? = null)

    fun schedule(task: TimerTask, delay: Duration, period: Duration? = null)

    fun scheduleFixed(task: TimerTask, instant: Instant, period: Duration? = null)

    fun scheduleFixed(task: TimerTask, delay: Duration, period: Duration? = null)
}

fun Timer(name: String? = null, daemon: Boolean = false): Timer {
    return createTimer(name, daemon)
}

internal expect fun createTimer(name: String?, daemon: Boolean): Timer
