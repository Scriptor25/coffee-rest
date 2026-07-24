package dev.scriptor.server

import java.util.logging.Level
import java.util.logging.Logger

inline fun Logger.trace(t: Throwable) {
    this.log(Level.SEVERE, t.stackTraceToString())
}
