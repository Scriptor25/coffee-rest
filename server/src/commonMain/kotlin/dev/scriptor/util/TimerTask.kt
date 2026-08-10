package dev.scriptor.util

interface TimerTask {

    companion object {
        fun from(callback: () -> Unit): TimerTask {
            return object : TimerTask {
                override fun run() {
                    callback()
                }
            }
        }
    }

    fun run()
    fun cancel() {}
}
