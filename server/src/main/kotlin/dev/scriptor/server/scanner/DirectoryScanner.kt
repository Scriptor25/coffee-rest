package dev.scriptor.server.scanner

import java.io.File

class DirectoryScanner(private val root: File) : ClassPathScanner {

    override fun scan(packageName: String?): Sequence<String> {
        val begin =
            if (packageName.isNullOrEmpty()) root
            else File(root, packageName.replace('.', File.separatorChar))

        if (!begin.exists()) {
            return emptySequence()
        }

        return begin
            .walkTopDown()
            .filter { it.isFile && it.extension == "class" }
            .map {
                val relative = it.relativeTo(root).path

                relative
                    .removeSuffix(".class")
                    .replace(File.separatorChar, '.')
            }
    }
}