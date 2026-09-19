package dev.scriptor.server.security

data object DefaultAuthorizer : Authorizer {

    override fun authorize(
        principal: Principal?,
        policy: SecurityPolicy,
    ): AuthorizationResult {
        for (requirement in policy.requirements) {
            when (requirement) {
                SecurityRequirement.Authenticated -> {
                    if (principal == null) {
                        return AuthorizationResult.Unauthenticated
                    }
                }

                is SecurityRequirement.Role -> {
                    if (principal == null) {
                        return AuthorizationResult.Unauthenticated
                    }

                    if (requirement.role !in principal.roles) {
                        return AuthorizationResult.Forbidden
                    }
                }
            }
        }

        return AuthorizationResult.Allowed
    }
}
