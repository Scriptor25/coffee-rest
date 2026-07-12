package io.scriptor.rest

import io.scriptor.annotation.Endpoint
import io.scriptor.annotation.Resource
import java.io.InputStream

@Endpoint("/")
class RootRest {

    @Resource(path = "favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream? = ClassLoader.getSystemResourceAsStream("favicon.svg")
}
