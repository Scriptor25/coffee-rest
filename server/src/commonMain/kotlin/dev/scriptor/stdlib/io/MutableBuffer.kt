package dev.scriptor.stdlib.io

class MutableBuffer(capacity: Int) : Buffer(ByteArray(capacity)) {

    operator fun set(index: Int, value: Byte) {
        if (index !in 0 until limit)
            error("index out of bounds")
        array[index] = value
    }

    fun push(value: Byte): MutableBuffer {
        this[position++] = value
        return this
    }

    fun push(buffer: Buffer): MutableBuffer {
        while (buffer.remaining != 0) {
            push(buffer.pop())
        }
        return this
    }

    override fun clear(): MutableBuffer {
        super.clear()
        return this
    }

    override fun flip(): MutableBuffer {
        super.flip()
        return this
    }
}
