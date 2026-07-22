package dev.scriptor.example

import dev.scriptor.server.scan

fun main() {
    scan(packageName = "dev.scriptor").use { it.start() }
}
