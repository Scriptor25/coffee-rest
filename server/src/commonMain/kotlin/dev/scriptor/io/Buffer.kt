package dev.scriptor.io

interface Buffer {

    companion object {
        fun allocate(capacity: Long): Buffer {
            TODO()
        }

        fun wrap(array: ByteArray): Buffer {
            TODO()
        }
    }

    val capacity: Long

    var position: Long
    var limit: Long

    val remaining: Long
        get() = limit - position

    fun get(): Byte

    operator fun get(index: Long): Byte

    fun clear(): Buffer {
        limit = capacity
        position = 0L
        return this
    }

    fun flip(): Buffer {
        limit = position
        position = 0L
        return this
    }

    fun rewind(): Buffer {
        position = 0L
        return this
    }
}
