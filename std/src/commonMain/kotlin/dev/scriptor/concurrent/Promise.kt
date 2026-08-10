package dev.scriptor.concurrent

import kotlin.time.Duration

interface Promise<T> {
    val done: Boolean
    val cancelled: Boolean

    fun get(): T
    fun get(timeout: Duration): T

    fun cancel(interrupt: Boolean): Boolean
}
