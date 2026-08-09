package dev.scriptor.stdlib.io

class Path {

    val absolute: Boolean
    val segments: List<String>

    internal constructor(
        absolute: Boolean,
        segments: List<String>,
    ) {
        this.absolute = absolute
        this.segments = segments
    }

    fun toAbsolutePath(): Path {
        if (absolute) return this
        throw Throwable("TODO: resolve path from current work directory")
    }
}

expect fun Path(filename: String): Path
expect fun Path(vararg elements: String): Path
