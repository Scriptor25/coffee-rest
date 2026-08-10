package dev.scriptor.io

actual fun getCurrentWorkingDirectory(): String {
    return kotlin.io.path.Path(".").toAbsolutePath().toString()
}
