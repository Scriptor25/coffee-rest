package dev.scriptor.server.http

import dev.scriptor.reflect.Type
import dev.scriptor.server.Parameter

data class RouteMetadata(
    val method: Method,
    val pathname: Pathname,
    val accept: String?,
    val result: String?,
    val parameters: List<Parameter>,
    val returns: Type,
    val callee: (Map<Int, Any?>) -> Any?,
) : Comparable<RouteMetadata> {

    override fun compareTo(other: RouteMetadata): Int {
        return pathname.compareTo(other.pathname)
    }

    override fun toString(): String {
        return "$method $pathname : $accept -> $result"
    }
}