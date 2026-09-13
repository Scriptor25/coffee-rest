package dev.scriptor.server.converter

import dev.scriptor.server.Provider
import dev.scriptor.server.http.MessageBody
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer

class MessageBodyInputStreamConverter : Converter<MessageBody, InputStream> {

    context(_: Provider?)
    override fun convert(value: MessageBody): InputStream = object : InputStream() {
        override fun read(): Int {
            val array = ByteArray(1)
            val count = readNBytes(array, 0, 1)
            return if (count == 1) array[0].toInt() else -1
        }

        override fun read(b: ByteArray): Int {
            return value.channel.read(ByteBuffer.wrap(b))
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            return value.channel.read(ByteBuffer.wrap(b, off, len))
        }

        override fun readAllBytes(): ByteArray {
            val bytes = ByteArray(8192)
            val chunk = ByteBuffer.wrap(bytes)
            val data = ByteArrayOutputStream()

            while (true) {
                val count = value.channel.read(chunk.clear())
                if (count == 0) continue
                if (count < 0) break
                data.write(bytes, 0, chunk.flip().remaining())
            }

            return data.toByteArray()
        }

        override fun readNBytes(b: ByteArray, off: Int, len: Int): Int {
            return value.channel.read(ByteBuffer.wrap(b, off, len))
        }

        override fun readNBytes(len: Int): ByteArray {
            val bytes = ByteArray(len)
            val count = readNBytes(bytes, 0, len)
            return if (count < 0) ByteArray(0) else bytes.copyOfRange(0, count)
        }
    }
}