package dev.scriptor.io

class Path : Iterable<String> {

    val segments: List<String>
    val absolute: Boolean

    internal constructor(
        segments: List<String>,
        absolute: Boolean,
    ) {
        this.segments = segments
        this.absolute = absolute
    }

    constructor() {
        this.segments = listOf(".")
        this.absolute = false
    }

    constructor(vararg segments: String) : this(segments.joinToString("/"))

    constructor(path: String) {
        val path = path.trim()
        this.segments = normalize(path.split("/", "\\"))
        this.absolute = path.isNotEmpty() && path.first() == '/'
    }

    fun toAbsolutePath(): Path {
        if (absolute) return this
        return resolve(getCurrentWorkingDirectory())
    }

    fun resolve(path: Path): Path {
        if (path.absolute) return path
        return Path(segments + path.segments, absolute)
    }

    fun resolve(vararg segments: String): Path {
        return resolve(Path(*segments))
    }

    override operator fun iterator(): Iterator<String> {
        return segments.iterator()
    }

    override fun toString(): String {
        return segments.joinToString("/", if (absolute) "/" else "")
    }
}

internal fun normalize(segments: List<String>): List<String> {
    return segments
        .map(String::trim)
        .filter(String::isNotEmpty)
        .filter { it != "." }
        .ifEmpty { listOf(".") }
}

expect fun getCurrentWorkingDirectory(): String
