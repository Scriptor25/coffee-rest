package dev.scriptor.server.jvm.scanner

interface ClassPathScanner {

    fun scan(packageName: String?): Sequence<String>
}