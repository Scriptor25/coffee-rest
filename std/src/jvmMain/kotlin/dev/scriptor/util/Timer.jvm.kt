package dev.scriptor.util

import kotlin.time.Duration
import kotlin.time.Instant

internal actual fun createTimer(name: String?, daemon: Boolean): Timer {
    val timer =
        if (name == null)
            java.util.Timer(daemon)
        else
            java.util.Timer(name, daemon)

    return object : Timer {

        override fun cancel() {
            timer.cancel()
        }

        override fun purge(): Int {
            return timer.purge()
        }

        override fun schedule(
            task: TimerTask,
            instant: Instant,
            period: Duration?,
        ) {
            timer.schedule(task, instant, period)
        }

        override fun schedule(
            task: TimerTask,
            delay: Duration,
            period: Duration?,
        ) {
            timer.schedule(task, delay, period)
        }

        override fun scheduleFixed(
            task: TimerTask,
            instant: Instant,
            period: Duration?,
        ) {
            timer.scheduleAtFixedRate(task, instant, period)
        }

        override fun scheduleFixed(
            task: TimerTask,
            delay: Duration,
            period: Duration?,
        ) {
            timer.scheduleAtFixedRate(task, delay, period)
        }
    }
}
