package dev.scriptor.concurrent

interface PromiseHandler<T, A> {
    fun completed(result: T, attachment: A)
    fun failed(cause: Throwable, attachment: A)
}