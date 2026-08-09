package dev.scriptor.server.result

import dev.scriptor.server.ParameterList
import dev.scriptor.stdlib.io.BufferChannel
import dev.scriptor.stdlib.io.ReadableChannel

class StringResult : Result {

    private companion object {

        data class CountChannel(
            val count: Long,
            val channel: ReadableChannel?,
        )

        fun create(value: String?): CountChannel {
            val count: Long
            val channel: ReadableChannel?
            if (value != null) {
                val array = value.encodeToByteArray()

                count = array.size.toLong()
                channel = BufferChannel(array)
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
