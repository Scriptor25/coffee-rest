package io.scriptor.log

import java.util.logging.Level
import java.util.logging.Logger

private val logger = Logger.getLogger("io.scriptor")

fun info(format: String, vararg arguments: Any?) {
    logger.info { format.format(*arguments) }
}

fun warning(format: String, vararg arguments: Any?) {
    logger.warning { format.format(*arguments) }
}

fun severe(format: String, vararg arguments: Any?) {
    logger.severe { format.format(*arguments) }
}

fun throwing(sourceClass: String, sourceMethod: String, thrown: Throwable) {
    logger.throwing(sourceClass, sourceMethod, thrown)
}

fun trace(e: Throwable) {
    logger.log(Level.SEVERE, e.message, e)
}