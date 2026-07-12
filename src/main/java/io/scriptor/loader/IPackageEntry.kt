package io.scriptor.loader

interface IPackageEntry {

    data class ClassEntry(val type: Class<*>) : IPackageEntry

    data class IterableEntry(val entries: MutableList<IPackageEntry>) : IPackageEntry, Iterable<IPackageEntry> {
        constructor() : this(ArrayList<IPackageEntry>())

        override fun iterator(): MutableIterator<IPackageEntry> {
            return entries.iterator()
        }
    }

    data class FutureEntry(val classLoader: ResourceClassLoader, val packageName: String) : IPackageEntry
}
