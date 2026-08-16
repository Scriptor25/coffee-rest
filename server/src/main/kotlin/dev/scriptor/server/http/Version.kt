package dev.scriptor.server.http

enum class Version(val value: String) {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1");

    override fun toString(): String = value
}
