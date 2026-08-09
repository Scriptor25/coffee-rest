package dev.scriptor.server.http

import dev.scriptor.server.Provider
import dev.scriptor.server.result.Result

data class Route(
    val callback: (Provider, Request) -> Result,
    val path: PathExpression,
    val method: Method,
    val accept: String,
    val result: String
) : Comparable<Route> {

    override fun compareTo(other: Route): Int {
        return this.path.compareTo(other.path)
    }

    override fun toString(): String {
        return "$method $path : $accept -> $result"
    }

    operator fun invoke(provider: Provider, request: Request): Result {
        return callback(provider, request)
    }
}
