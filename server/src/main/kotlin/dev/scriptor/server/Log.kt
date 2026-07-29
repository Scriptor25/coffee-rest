package dev.scriptor.server

import java.util.logging.Level
import java.util.logging.Logger

fun Logger.trace(t: Throwable) {
    this.log(Level.SEVERE, t.stackTraceToString())
}
