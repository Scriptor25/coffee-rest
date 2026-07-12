package io.scriptor.loader

import io.scriptor.loader.IPackageEntry.FutureEntry
import io.scriptor.loader.IPackageEntry.IterableEntry
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class DirectoryClassLoader(parentLoader: ClassLoader?, private val rootFile: File) : ResourceClassLoader(parentLoader) {

    override fun getClassPackage(packageName: String): IterableEntry {
        val filename = packageName.replace('.', File.separatorChar)
        val file = File(rootFile, filename)

        if (!file.exists() || !file.isDirectory) {
            return IterableEntry()
        }

        val files = file.listFiles() ?: return IterableEntry()

        val entries: MutableList<IPackageEntry> = ArrayList()

        for (entry in files) {
            val entryName: String = "%s.%s".format(packageName, entry.name.replace("\\.class".toRegex(), ""))

            if (!entry.name.endsWith(".class")) {
                entries.add(FutureEntry(this, entryName))
                continue
            }

            entries.add(IPackageEntry.ClassEntry(loadClass(entryName)))
        }

        return IterableEntry(entries)
    }

    override fun getResourceAsStream(fileName: String): InputStream? {
        return try {
            FileInputStream(File(rootFile, fileName))
        } catch (_: FileNotFoundException) {
            null
        }
    }
}
