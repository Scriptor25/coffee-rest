package io.scriptor.loader

import java.io.File

class Loader(classLoader: ResourceClassLoader, packageName: String) : Iterable<Class<*>?> {

    private val classes: ITree<Class<*>> = ClassTree(classLoader, packageName)

    constructor(packageName: String) : this(ResourceClassLoader(ClassLoader.getSystemClassLoader()), packageName)

    constructor(fileName: String, packageName: String) : this(
        DirectoryClassLoader(
            ClassLoader.getSystemClassLoader(),
            File(fileName)
        ), packageName
    )

    override fun iterator(): MutableIterator<Class<*>> {
        return this.classes.iterator()
    }
}
