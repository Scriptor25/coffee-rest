package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

class StringResult : Result {

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
        contentType: String,
        headers: ParameterList,
        countChannel: CountChannel,
    ) : super(
        statusCode,
        statusText,
        contentType,
        headers,
        countChannel.channel,
        0L,
        countChannel.count,
    )

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "text/plain",
        headers: ParameterList = ParameterList(),
        value: String? = null,
    ) : this(
        statusCode,
        statusText,
        contentType,
        headers,
        create(value),
    )
}
