package dev.scriptor.net

data class URI(
    val scheme: String,
    val ssp: String = "//",
    val username: String? = null,
    val hostname: String,
    val port: Int? = null,
    val pathname: String = "",
    val query: String = "",
    val fragment: String = "",
) {
    companion object {
        fun parse(uri: String): URI {
            TODO()
        }
    }

    override fun toString(): String {
        return buildString {
            append(scheme)
            append(":")
            append(ssp)
            if (username != null) {
                append(username)
                append("@")
            }
            append(hostname)
            if (port != null) {
                append(port)
            }
            append(pathname)
            if (query.isNotEmpty()) {
                append("?")
                append(query)
            }
            if (fragment.isNotEmpty()) {
                append("#")
                append(fragment)
            }
        }
    }
}
