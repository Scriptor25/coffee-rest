package dev.scriptor.example.rest

import dev.scriptor.server.jvm.annotation.Get

@Get("/foo", result = "text/plain")
@Suppress("unused")
fun foo(): String = "foo"
