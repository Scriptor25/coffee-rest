package dev.scriptor.server.security

sealed interface SecurityRequirement {

    data object Authenticated : SecurityRequirement

    data class Role(val role: String) : SecurityRequirement
}
