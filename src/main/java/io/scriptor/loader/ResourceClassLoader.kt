package io.scriptor.loader

import io.scriptor.loader.IPackageEntry.FutureEntry
import io.scriptor.loader.IPackageEntry.IterableEntry
import io.scriptor.log.info
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

open class ResourceClassLoader(parentLoader: ClassLoader?) : ClassLoader(parentLoader) {

    open fun getClassPackage(packageName: String): IterableEntry {
        val filename = packageName.replace('.', File.separatorChar)

        getResourceAsStream(filename).use { packageStream ->
            if (packageStream == null) {
                return IterableEntry()
            }

            val entries: MutableList<IPackageEntry> = ArrayList()

            val reader = BufferedReader(InputStreamReader(packageStream))
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                val lineNoClass = line!!.replace("\\.class".toRegex(), "")
                val entryName =
                    if (packageName.isEmpty()) lineNoClass
                    else "%s.%s".format(packageName, lineNoClass)

                if (!line.endsWith(".class")) {
                    entries.add(FutureEntry(this, entryName))
                    continue
                }

                entries.add(IPackageEntry.ClassEntry(loadClass(entryName)))
            }
            return IterableEntry(entries)
        }
    }

    override fun findClass(className: String): Class<*> {
        info("query class %s", className)
        val fileName = className.replace('.', File.separatorChar) + ".class"
        try {
            getResourceAsStream(fileName).use { classStream ->
                val classBytes = classStream!!.readAllBytes()
                return defineClass(className, classBytes, 0, classBytes.size)
            }
        } catch (e: IOException) {
            throw ClassNotFoundException(e.message)
        }
    }
}
