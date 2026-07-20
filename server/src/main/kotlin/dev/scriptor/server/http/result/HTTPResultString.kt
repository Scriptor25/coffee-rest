package dev.scriptor.server.http.result

import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

class HTTPResultString : HTTPResult<String> {

    private companion object {

        data class CountChannel(val count: Long, val channel: ReadableByteChannel?)

        fun create(value: String?): CountChannel {
            val count: Long
            val channel: ReadableByteChannel?
            if (value != null) {
                val buf = value.encodeToByteArray()

                count = buf.size.toLong()
                channel = Channels.newChannel(buf.inputStream())
            } else {
                count = 0L
                channel = null
            }
            return CountChannel(count, channel)
        }
    }

    private constructor(
        statusCode: Int,
        statusText: String,
        headers: Map<String, String>,
        countChannel: CountChannel,
    ) : super(statusCode, statusText, headers, 0L, countChannel.count, countChannel.channel)

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        headers: Map<String, String> = emptyMap(),
        value: String? = null,
    ) : this(statusCode, statusText, headers, create(value))
}
