package dev.scriptor.server.security

import dev.scriptor.server.request.Request

interface Authenticator {

    fun authenticate(request: Request): Principal?
}
