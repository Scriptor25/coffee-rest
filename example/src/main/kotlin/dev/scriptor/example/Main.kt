package dev.scriptor.example

import dev.scriptor.server.http.Server
import dev.scriptor.server.jvm.scan
import java.net.InetAddress
import java.util.logging.*

fun main() {
    val log = Logger.getLogger("example")
    log.level = Level.ALL

    val handler = ConsoleHandler()
    handler.level = log.level
    handler.formatter = object : Formatter() {
        override fun format(record: LogRecord): String {
            return "[${record.loggerName}][${record.level}][${record.instant}] ${record.message}\n"
        }
    }

    log.useParentHandlers = false
    log.addHandler(handler)

    val server = Server(log, addr = InetAddress.getLocalHost(), port = 8080)

    server.use { server ->
        scan(server, "dev.scriptor")

        server.start()
    }
}
