package dev.scriptor.example.rest

import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.Resource
import java.io.InputStream

@Endpoint("/")
class RootRest {

    @Resource(path = "/favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream? = ClassLoader.getSystemResourceAsStream("favicon.svg")
}
