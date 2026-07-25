package dev.scriptor.example

import dev.scriptor.server.http.HTTPServer
import dev.scriptor.server.scan
import java.util.logging.*

fun main() {
    val log = Logger.getLogger("custom")
    log.level = Level.CONFIG

    val handler = ConsoleHandler()
    handler.level = log.level
    handler.formatter = object : Formatter() {

        override fun format(record: LogRecord?): String? {
            if (record == null) return null

            return "[${record.level}][${record.instant}][${record.sourceClassName}.${record.sourceMethodName}(...)] ${record.message}\n"
        }
    }

    log.useParentHandlers = false
    log.addHandler(handler)

    val server = HTTPServer(log, "0.0.0.0", 8080)

    server.use {
        scan(server, "dev.scriptor")

        it.inject("log", log)
        it.start()
    }
}
