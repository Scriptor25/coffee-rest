package dev.scriptor.example.rest

import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.annotation.Controller
import dev.scriptor.server.annotation.Get
import dev.scriptor.server.annotation.PathParameter
import java.io.InputStream
import java.util.logging.Logger

@Controller("/")
class RootRest {

    @Get("/favicon.[]", result = "image/svg+xml")
    fun getFavicon(): InputStream =
        ClassLoader.getSystemResourceAsStream("favicon.svg")
            ?: throw NotFoundSignal()

    @Get("/[slug+]")
    context(log: Logger)
    fun getSomething(@PathParameter slug: Array<String>) {
        log.info(slug.contentToString())
    }
}
