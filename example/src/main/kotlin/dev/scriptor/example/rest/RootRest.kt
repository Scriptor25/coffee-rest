package dev.scriptor.example.rest

import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.PathParameter
import dev.scriptor.server.annotation.Resource
import dev.scriptor.util.Log
import java.io.InputStream

@Endpoint("/")
class RootRest {

    @Resource("/favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream =
        ClassLoader.getSystemResourceAsStream("favicon.svg")
            ?: throw NotFoundSignal()

    @Resource("/[slug+]")
    context(log: Log)
    fun getSomething(@PathParameter slug: Array<String>) {
        log.info(slug.contentToString())
    }
}
