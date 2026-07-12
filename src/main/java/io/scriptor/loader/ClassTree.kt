package io.scriptor.loader

import java.io.IOException
import java.util.*

data class ClassTree(val classLoader: ResourceClassLoader, val packageName: String) : ITree<Class<*>> {

    private data class ClassEntry(override val value: Class<*>) : ITree<Class<*>> {

        override val children: MutableIterator<ITree<Class<*>>>
            get() = Collections.emptyIterator()

        override fun hasValue(): Boolean {
            return true
        }
    }

    override val children
        get(): MutableIterator<ITree<Class<*>>> {
            return object : MutableIterator<ITree<Class<*>>> {
                private val iterator: MutableIterator<IPackageEntry>

                init {
                    try {
                        iterator = classLoader.getClassPackage(packageName).iterator()
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    } catch (e: ClassNotFoundException) {
                        throw RuntimeException(e)
                    }
                }

                override fun hasNext(): Boolean {
                    return iterator.hasNext()
                }

                override fun next(): ITree<Class<*>> {
                    val entry = iterator.next()
                    if (entry is IPackageEntry.ClassEntry) {
                        return ClassEntry(entry.type)
                    }
                    if (entry is IPackageEntry.FutureEntry) {
                        return ClassTree(entry.classLoader, entry.packageName)
                    }
                    throw NoSuchElementException()
                }

                override fun remove() {
                    throw UnsupportedOperationException()
                }
            }
        }

    override val value = null

    override fun hasValue(): Boolean {
        return false
    }
}
