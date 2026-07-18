package dev.scriptor.server.scanner

import java.io.File
import java.util.zip.ZipFile

class JarScanner(private val jar: File) : ClassPathScanner {

    override fun scan(packageName: String?): Sequence<String> {
        val prefix =
            if (packageName.isNullOrEmpty()) null
            else packageName.replace('.', '/') + '/'

        return sequence {
            ZipFile(jar).use { zip ->
                val entries = zip.entries()

                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val name = entry.name

                    if (!name.endsWith(".class")) {
                        continue
                    }

                    if (prefix != null && !name.startsWith(prefix)) {
                        continue
                    }

                    yield(
                        name
                            .removeSuffix(".class")
                            .replace('/', '.')
                    )
                }
            }
        }
    }
}