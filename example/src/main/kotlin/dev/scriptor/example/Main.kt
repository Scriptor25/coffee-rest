package dev.scriptor.example

import dev.scriptor.server.http.Server
import dev.scriptor.server.util.Log

fun main() {
    val log = Log.create("example", Log.Level.ALL)

    val server = Server(log)

    server.use { server -> server.start() }
}
