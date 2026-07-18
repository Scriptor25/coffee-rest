package dev.scriptor.server.http.result

import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

class HTTPResultChannel : HTTPResult<ReadableByteChannel> {

    override val size = -1
    override val stream = if (body !== null) (object : InputStream() {

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            if (len == 0) {
                return 0
            }

            if (body.isOpen) {
                val buffer = ByteBuffer.wrap(b).limit(off + len).position(off)
                return body.read(buffer)
            }

            return -1
        }

        override fun transferTo(out: OutputStream): Long {
            var count = 0L

            while (body.isOpen) {
                val buffer = ByteBuffer.wrap(ByteArray(1024))
                val read = body.read(buffer)

                if (read < 0) {
                    break
                }

                out.write(buffer.array(), 0, read)
                count += read.toLong()
            }

            return count
        }

        override fun read(): Int {
            if (body.isOpen) {
                val buffer = ByteBuffer.allocate(1)
                body.read(buffer)
                return buffer.get(0).toInt()
            }

            return -1
        }
    }) else null

    constructor(statusCode: Int) : super(statusCode)

    constructor(statusCode: Int, statusText: String) : super(statusCode, statusText)

    constructor(
        statusCode: Int,
        statusText: String,
        value: ReadableByteChannel
    ) : super(statusCode, statusText, value)

    constructor(statusCode: Int, headers: Map<String, String>) : super(statusCode, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>
    ) : super(statusCode, statusText, headers)

    constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>,
        value: ReadableByteChannel
    ) : super(statusCode, statusText, headers, value)
}
