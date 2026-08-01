package dev.scriptor.server.http

import kotlin.reflect.KCallable

data class Route(
    val instance: Any,
    val callee: KCallable<*>,
    val pathname: Pathname,
    val method: Method,
    val accept: String,
    val result: String
) : Comparable<Route> {

    override fun compareTo(other: Route): Int {
        return this.pathname.compareTo(other.pathname)
    }

    override fun toString(): String {
        return "$method $pathname : $accept -> $result"
    }
}