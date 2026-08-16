package dev.scriptor.server.http

import kotlin.reflect.KCallable

data class RouteMetadata(
    val instance: Any?,
    val callee: KCallable<*>,
    val pathname: Pathname,
    val method: Method,
    val accept: String,
    val result: String
) : Comparable<RouteMetadata> {

    override fun compareTo(other: RouteMetadata): Int {
        return this.pathname.compareTo(other.pathname)
    }

    override fun toString(): String {
        return "$method $pathname : $accept -> $result"
    }
}