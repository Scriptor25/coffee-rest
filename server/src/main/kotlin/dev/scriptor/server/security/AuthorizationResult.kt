package dev.scriptor.server.security

sealed interface AuthorizationResult {

    data object Allowed : AuthorizationResult
    data object Unauthenticated : AuthorizationResult
    data object Forbidden : AuthorizationResult
}
