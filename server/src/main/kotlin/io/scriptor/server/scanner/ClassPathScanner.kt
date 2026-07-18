package io.scriptor.server.scanner

interface ClassPathScanner {

    fun scan(packageName: String): Sequence<String>
}