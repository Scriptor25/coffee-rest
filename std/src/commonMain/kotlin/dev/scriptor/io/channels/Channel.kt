package dev.scriptor.io.channels

interface Channel : AutoCloseable {
    val open: Boolean
}
