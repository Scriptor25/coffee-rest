package dev.scriptor.io.channels

import dev.scriptor.concurrent.Promise
import dev.scriptor.concurrent.PromiseHandler
import dev.scriptor.io.Buffer
import dev.scriptor.io.MutableBuffer

interface AsynchronousByteChannel : AsynchronousChannel {
    fun read(destination: MutableBuffer): Promise<Long>
    fun <A> read(destination: MutableBuffer, attachment: A, handler: PromiseHandler<Long, A>)
    fun write(source: Buffer): Promise<Long>
    fun <A> write(source: Buffer, attachment: A, handler: PromiseHandler<Long, A>)
}
