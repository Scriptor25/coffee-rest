package dev.scriptor.server.security

import kotlin.uuid.Uuid

data class Principal(
    val id: Uuid,
    val roles: Set<String> = emptySet(),
)
