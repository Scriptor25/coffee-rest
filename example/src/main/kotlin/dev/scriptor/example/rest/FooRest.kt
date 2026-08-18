package dev.scriptor.example.rest

import dev.scriptor.server.annotation.Get

@Get("/foo", result = "text/plain")
fun foo(): String = "foo"
