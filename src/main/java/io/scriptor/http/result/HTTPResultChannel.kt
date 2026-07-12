package io.scriptor.http.result

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

class HTTPResultChannel : HTTPResult<ReadableByteChannel> {

    override val size = -1
    override val stream = object : InputStream() {

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            if (len == 0) {
                return 0
            }

            if (getBody().isOpen) {
                val buffer = ByteBuffer.wrap(b).limit(off + len).position(off)
                return getBody().read(buffer)
            }

            return -1
        }

        override fun transferTo(out: OutputStream): Long {
            var count: Long = 0

            while (getBody().isOpen) {
                val buffer = ByteBuffer.wrap(ByteArray(1024))
                val read = getBody().read(buffer)

                if (read < 0) {
                    break
                }

                out.write(buffer.array(), 0, read)
                count += read.toLong()
            }

            return count
        }

        override fun read(): Int {
            if (getBody().isOpen) {
                val buffer = ByteBuffer.allocate(1)
                getBody().read(buffer)
                return buffer.get(0).toInt()
            }

            return -1
        }
    }

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(
        statusCode: Int,
        statusText: String,
        value: ReadableByteChannel
    ) : super(statusCode, statusText, value)

    constructor(statusCode: Int, headers: MutableMap<String, String>) : super(statusCode, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: MutableMap<String, String>
    ) : super(statusCode, statusText, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: MutableMap<String, String>,
        value: ReadableByteChannel
    ) : super(statusCode, statusText, headers, value)
}
