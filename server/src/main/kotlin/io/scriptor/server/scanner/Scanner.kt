package io.scriptor.server.scanner

class Scanner(
    private val packageName: String,
    private val loader: ClassLoader = Thread.currentThread().contextClassLoader,
) : Iterable<Class<*>> {

    override fun iterator(): Iterator<Class<*>> {
        val names = ClassPathScannerFactory
            .scanners
            .flatMap { it.scan(packageName) }

        return names
            .map { Class.forName(it, false, loader) }
            .iterator()
    }
}
