package dev.scriptor.server.scanner

import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

object ClassPathScannerFactory {

    val scanners: List<ClassPathScanner>
        get() = ClassPath.entries.mapNotNull {
            when {
                it.isDirectory() -> DirectoryScanner(it)
                it.isRegularFile() && it.extension == "jar" -> JarScanner(it)
                else -> null
            }
        }
}
