package dev.scriptor.util

import dev.scriptor.time.Duration
import dev.scriptor.time.Instant

class Timer {

    constructor(name: String? = null, daemon: Boolean = false) {
        TODO()
    }

    fun cancel() {
        TODO()
    }

    fun purge(): Int {
        TODO()
    }

    fun schedule(task: TimerTask, instant: Instant, period: Duration? = null) {
        TODO()
    }

    fun schedule(task: TimerTask, delay: Duration, period: Duration? = null) {
        TODO()
    }

    fun scheduleFixed(task: TimerTask, instant: Instant, period: Duration? = null) {
        TODO()
    }

    fun scheduleFixed(task: TimerTask, delay: Duration, period: Duration? = null) {
        TODO()
    }
}
