package dev.scriptor.example

import dev.scriptor.server.scan
import java.util.logging.Logger

fun main() {
    val log = Logger.getLogger("custom")
    val server = scan(log, packageName = "dev.scriptor")

    server.registerValue("log", log)

    server.use { it.start() }
}
