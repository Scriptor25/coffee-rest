package dev.scriptor.io

interface Buffer {

    companion object {
        fun wrap(array: ByteArray): Buffer {
            return object : Buffer {
                override val capacity: Int = array.size
                override var position: Int = 0
                override var limit: Int = capacity

                override fun get(): Byte {
                    if (position !in 0L until limit) {
                        error("buffer underflow")
                    }
                    return array[position++]
                }

                override fun get(index: Int): Byte {
                    if (index !in 0L until limit) {
                        error("index out of bounds")
                    }
                    return array[index]
                }
            }
        }
    }

    val capacity: Int

    var position: Int
    var limit: Int

    val remaining: Int
        get() = limit - position

    fun get(): Byte

    operator fun get(index: Int): Byte

    fun clear(): Buffer {
        limit = capacity
        position = 0
        return this
    }

    fun flip(): Buffer {
        limit = position
        position = 0
        return this
    }

    fun rewind(): Buffer {
        position = 0
        return this
    }
}
