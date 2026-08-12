package dev.scriptor.io

interface MutableBuffer : Buffer {

    companion object {
        fun allocate(capacity: Int): MutableBuffer {
            return allocateMutableBuffer(capacity)
        }

        fun wrap(array: ByteArray): MutableBuffer {
            return object : MutableBuffer {

                override val capacity: Int = array.size
                override var position: Int = 0
                override var limit: Int = capacity

                override fun get(): Byte {
                    if (position !in 0 until limit) {
                        error("buffer underflow")
                    }
                    return array[position++]
                }

                override fun get(index: Int): Byte {
                    if (index !in 0 until limit) {
                        error("index out of bounds")
                    }
                    return array[index]
                }

                override fun put(value: Byte) {
                    if (position !in 0 until limit) {
                        error("buffer overflow")
                    }
                    array[position++] = value
                }

                override fun set(index: Int, value: Byte) {
                    if (index !in 0 until limit) {
                        error("index out of bounds")
                    }
                    array[index] = value
                }
            }
        }
    }

    fun put(value: Byte)

    operator fun set(index: Int, value: Byte)

    override fun clear(): MutableBuffer {
        super.clear()
        return this
    }

    override fun flip(): MutableBuffer {
        super.flip()
        return this
    }

    override fun rewind(): MutableBuffer {
        super.rewind()
        return this
    }
}

internal expect fun allocateMutableBuffer(capacity: Int): MutableBuffer
