package dev.scriptor.server.scanner

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

object ClassPath {

    val entries: List<Path>
        get() {
            return System
                .getProperty("java.class.path")
                .split("/", "\\")
                .map(::Path)
                .filter { it.exists() }
        }
}
