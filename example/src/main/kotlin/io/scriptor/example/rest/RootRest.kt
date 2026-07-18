package io.scriptor.example.rest

import io.scriptor.server.annotation.Endpoint
import io.scriptor.server.annotation.Resource
import java.io.InputStream

@Endpoint("/")
class RootRest {

    @Resource(path = "favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream? = ClassLoader.getSystemResourceAsStream("favicon.svg")
}
