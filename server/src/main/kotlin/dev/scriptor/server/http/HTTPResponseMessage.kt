package dev.scriptor.server.http

import java.io.InputStream
import java.io.OutputStream

data class HTTPResponseMessage(
    val protocol: String,
    val statusCode: Int,
    val statusText: String,
    val headers: Map<String, String>,
    val body: InputStream?,
    val chunked: Boolean
) {
    fun write(stream: OutputStream) {
        writeString(stream, "%s %s %s\r\n".format(protocol, statusCode, statusText))
        for ((key, value) in headers) {
            writeString(stream, "%s: %s\r\n".format(key, value))
        }
        writeString(stream, "\r\n")
        stream.flush()

        if (body !== null) {
            if (!chunked) {
                body.transferTo(stream)
                stream.flush()
            } else {
                val buffer = ByteArray(1024)

                while (true) {
                    val count = body.read(buffer)

                    if (count < 0) {
                        break
                    }

                    writeString(stream, "%x\r\n".format(count))
                    stream.write(buffer, 0, count)
                    writeString(stream, "\r\n")
                    stream.flush()
                }

                writeString(stream, "0\r\n\r\n")
                stream.flush()
            }
        }
    }

    companion object {
        private fun writeString(stream: OutputStream, value: String) {
            for (b in value.encodeToByteArray()) {
                stream.write(b.toInt())
            }
        }
    }
}
