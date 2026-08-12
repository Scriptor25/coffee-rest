package dev.scriptor.util

import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toJavaDuration

fun TimerTask.toJavaTimerTask(): java.util.TimerTask {
    TODO()
}

fun Instant.toJavaDate(): java.util.Date {
    TODO()
}

fun Duration.toJavaMilliseconds(): Long {
    TODO()
}

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
            if (period == null)
                timer.schedule(
                    task.toJavaTimerTask(),
                    instant.toJavaDate(),
                )
            else
                timer.schedule(
                    task.toJavaTimerTask(),
                    instant.toJavaDate(),
                    period.toJavaMilliseconds(),
                )
        }

        override fun schedule(
            task: TimerTask,
            delay: Duration,
            period: Duration?,
        ) {
            if (period == null)
                timer.schedule(
                    task.toJavaTimerTask(),
                    delay.toJavaMilliseconds(),
                )
            else
                timer.schedule(
                    task.toJavaTimerTask(),
                    delay.toJavaMilliseconds(),
                    period.toJavaMilliseconds(),
                )
        }

        override fun scheduleFixed(
            task: TimerTask,
            instant: Instant,
            period: Duration,
        ) {
           timer.scheduleAtFixedRate(
                task.toJavaTimerTask(),
                instant.toJavaDate(),
                period.toJavaMilliseconds(),
            )
        }

        override fun scheduleFixed(
            task: TimerTask,
            delay: Duration,
            period: Duration,
        ) {
            timer.scheduleAtFixedRate(
                task.toJavaTimerTask(),
                delay.toJavaMilliseconds(),
                period.toJavaMilliseconds(),
            )
        }
    }
}
