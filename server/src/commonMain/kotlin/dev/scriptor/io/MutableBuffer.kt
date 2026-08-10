package dev.scriptor.io

interface MutableBuffer : Buffer {

    companion object {
        fun allocate(capacity: Long): MutableBuffer {
            TODO()
        }

        fun wrap(array: ByteArray): MutableBuffer {
            TODO()
        }
    }

    fun put(value: Byte)

    operator fun set(index: Long, value: Byte)

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
