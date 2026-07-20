package dev.scriptor.example

import dev.scriptor.server.scan

fun getEnv(key: String, value: String): String {
    val entry = System.getenv(key)
    return entry ?: value
}

fun main() {
    val port = getEnv("PORT", "8080").toInt()

    scan(port, "dev.scriptor").use { it.start() }
}
