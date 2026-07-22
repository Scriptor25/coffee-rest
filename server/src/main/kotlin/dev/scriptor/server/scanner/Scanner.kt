package dev.scriptor.server.scanner

import kotlin.reflect.KClass

class Scanner(
    private val packageName: String? = null,
    private val loader: ClassLoader = Thread.currentThread().contextClassLoader,
) : Iterable<KClass<*>> {

    override fun iterator(): Iterator<KClass<*>> {
        val names = ClassPathScannerFactory
            .scanners
            .flatMap { it.scan(packageName) }

        return names
            .map { Class.forName(it, false, loader).kotlin }
            .iterator()
    }
}
