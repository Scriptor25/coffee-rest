package dev.scriptor.example.rest

import dev.scriptor.server.annotation.Get

@Get("/foo")
fun foo(): String = "foo"
