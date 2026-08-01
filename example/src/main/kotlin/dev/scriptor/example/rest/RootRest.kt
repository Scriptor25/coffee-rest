package dev.scriptor.example.rest

import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.PathParameter
import dev.scriptor.server.annotation.Resource
import java.io.InputStream
import java.util.logging.Logger

@Endpoint("/")
class RootRest {

    @Resource("/favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream =
        ClassLoader.getSystemResourceAsStream("favicon.svg")
            ?: throw NotFoundSignal()

    @Resource("/[slug+]")
    context(log: Logger)
    fun getSomething(@PathParameter slug: Array<String>) {
        log.info(slug.contentToString())
    }
}
