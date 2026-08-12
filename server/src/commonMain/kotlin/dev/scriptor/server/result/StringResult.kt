package dev.scriptor.server.result

import dev.scriptor.io.channels.ReadableBufferChannel
import dev.scriptor.io.channels.ReadableByteChannel
import dev.scriptor.server.ParameterList

class StringResult : Result {

    private companion object {

        data class CountChannel(
            val count: Long,
            val channel: ReadableByteChannel?,
        )

        fun create(value: String?): CountChannel {
            val count: Long
            val channel: ReadableByteChannel?
            if (value != null) {
                val array = value.encodeToByteArray()

                count = array.size.toLong()
                channel = ReadableBufferChannel(array)
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
