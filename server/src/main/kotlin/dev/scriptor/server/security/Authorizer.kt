package dev.scriptor.server.security

interface Authorizer {

    fun authorize(
        principal: Principal?,
        policy: SecurityPolicy,
    ): AuthorizationResult
}
