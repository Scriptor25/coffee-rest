package dev.scriptor.stdlib.net

data class URI(
    val path: String,
    val query: String?,
) {
    constructor(uri: String) : this(TODO(), TODO())
}
