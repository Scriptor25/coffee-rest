package dev.scriptor.io

interface Path {

    companion object {
        fun parse(path: String): Path {
            TODO()
        }
    }

    fun toAbsolutePath(): Path
}
