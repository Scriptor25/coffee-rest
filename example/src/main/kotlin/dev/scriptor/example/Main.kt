package dev.scriptor.example

import dev.scriptor.server.scan

fun getEnv(key: String): String? {
    return System.getenv(key)
}

fun getEnv(key: String, value: String): String {
    val entry = System.getenv(key)
    return entry ?: value
}

fun main() {
    val enableTLS = getEnv("ENABLE_TLS", "0").toInt() != 0
    val port = getEnv("PORT", if (enableTLS) "8443" else "8080").toInt()
    val keystoreFilename = getEnv("KEYSTORE")
    val keystorePassphrase = getEnv("KEYSTORE_PASSPHRASE")

    scan(port, enableTLS, keystoreFilename, keystorePassphrase).use { it.start() }
}
