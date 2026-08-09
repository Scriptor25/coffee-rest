package dev.scriptor.stdlib.io

open class Buffer(
    protected val array: ByteArray,
) {
    var position: Int = 0
    var limit: Int = array.size

    val size: Int
        get() = array.size

    val remaining: Int
        get() = limit - position

    operator fun get(index: Int): Byte {
        if (index !in 0 until limit)
            error("index out of bounds")
        return array[index]
    }

    fun pop(): Byte {
        return this[position++]
    }

    open fun clear(): Buffer {
        position = 0
        limit = array.size
        return this
    }

    open fun flip(): Buffer {
        limit = position
        position = 0
        return this
    }
}
