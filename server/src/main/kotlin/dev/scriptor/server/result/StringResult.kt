package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import dev.scriptor.server.RangeReadableByteChannel
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

class StringResult : Result {

    private companion object {

        fun channel(value: String): RangeReadableByteChannel {
            val count: Long
            val channel: ReadableByteChannel?
            val array = value.encodeToByteArray()

            channel = Channels.newChannel(array.inputStream())
            count = array.size.toLong()

            return RangeReadableByteChannel(channel, 0L until count)
        }
    }

    constructor(
        statusCode: Int = 200,
        statusText: String = "OK",
        contentType: String = "text/plain",
        headers: ParameterList = ParameterList(),
        value: String,
    ) : super(
        statusCode,
        statusText,
        contentType,
        headers,
        channel(value),
    )
}
