package dev.scriptor.stdlib.sys

data class Thread(val run: () -> Unit) {

    companion object {
        val current: Thread
            get() = TODO()
    }

    val interrupted: Boolean
        get() = TODO()

    fun start() {
        TODO()
    }
}
