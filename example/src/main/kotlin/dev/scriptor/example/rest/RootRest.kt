package dev.scriptor.example.rest

import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.annotation.Endpoint
import dev.scriptor.server.annotation.Inject
import dev.scriptor.server.annotation.PathParameter
import dev.scriptor.server.annotation.Resource
import java.io.InputStream
import java.util.logging.Logger

@Endpoint("/")
class RootRest {

    @Inject("log")
    lateinit var log: Logger

    @Resource(path = "/favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream =
        ClassLoader.getSystemResourceAsStream("favicon.svg")
            ?: throw NotFoundSignal()

    @Resource(path = "/[slug+]")
    fun getSomething(@PathParameter("slug") slug: Array<String>) {
        log.info(slug.contentToString())
    }
}
