package dev.scriptor.io

fun transfer(destination: MutableBuffer, source: Buffer): Long {
    val count = minOf(source.remaining, destination.remaining)

    for (position in 0 until count) {
        destination[destination.position + position] = source[source.position + position]
    }

    destination.position += count
    source.position += count

    return count
}
