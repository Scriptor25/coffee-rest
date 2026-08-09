package dev.scriptor.server.scanner

import java.io.File
import java.nio.file.Path
import kotlin.io.path.*

class DirectoryScanner(private val root: Path) : ClassPathScanner {

    override fun scan(packageName: String?): Sequence<String> {
        val begin =
            if (packageName.isNullOrEmpty()) root
            else root.resolve(packageName.replace('.', File.separatorChar))

        if (!begin.exists()) {
            return emptySequence()
        }

        return begin
            .walk()
            .filter { it.isRegularFile() && it.extension == "class" }
            .map {
                val relative = it.relativeTo(root).pathString

                relative
                    .removeSuffix(".class")
                    .replace(File.separatorChar, '.')
            }
    }
}
