package dev.scriptor.server.scanner

import java.io.File

object ClassPath {

    fun entries(): List<File> {
        return System
            .getProperty("java.class.path")
            .split(File.pathSeparator)
            .map(::File)
            .filter(File::exists)
    }
}