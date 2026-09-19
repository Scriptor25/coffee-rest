package dev.scriptor.server.security

data class SecurityPolicy(
    val requirements: Set<SecurityRequirement>,
) {
    companion object {
        val Public = SecurityPolicy(emptySet())
    }

    val isPublic: Boolean
        get() = requirements.isEmpty()
}
