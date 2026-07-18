package dev.scriptor.server.http

import java.io.InputStream
import java.io.OutputStream

data class HTTPResponseMessage(
    val protocol: String,
    val statusCode: Int,
    val statusText: String,
    val headers: MutableMap<String, String>,
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

        if (body != null) {
            if (!chunked) {
                body.transferTo(stream)
                stream.flush()
            } else {
                var n: Int
                while ((body.available().also { n = it }) > 0) {
                    writeString(stream, "%x\r\n".format(n))
                    stream.write(body.readNBytes(n))
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
            for (b in value.toByteArray()) {
                stream.write(b.toInt())
            }
        }
    }
}
