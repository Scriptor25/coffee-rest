package dev.scriptor.concurrent

interface Promise<T> {
    val done: Boolean
    val cancelled: Boolean

    fun get(): T
    fun get(timeout: Long): T

    fun cancel(interrupt: Boolean): Boolean
}
