package dev.scriptor.example.rest

import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.jvm.annotation.*
import dev.scriptor.ui.Bundle
import dev.scriptor.ui.css.builder.*
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.util.logging.Logger

@Controller("/my")
class MyRest {

    @Get("/hello", result = "text/html")
    fun getHello(): InputStream =
        ClassLoader.getSystemResourceAsStream("hello.html")
            ?: throw NotFoundSignal()

    @Post("/message/[from]/[to]", accept = "text/plain")
    context(log: Logger)
    fun postMessage(
        @PathParameter from: String,
        @PathParameter to: String,
        @Header("content-length") contentLength: Int,
        @Body body: InputStream,
    ) {
        val bytes = body.readNBytes(contentLength)
        val message = bytes.decodeToString()
        log.info("message (from $from to $to): $message")
    }

    @Get("/random-quote", result = "text/html")
    context(log: Logger)
    fun getRandomQuote(): String {
        val url = URI("https://dummyjson.com/quotes/random").toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val text = connection.getInputStream().readAllBytes().decodeToString()
        val json = JSONObject(text)

        log.info(text)

        val author = json["author"]
        val quote = json["quote"]

        return Bundle().html {
            head {
                meta(charset = "utf-8")
                title("$author | Random Quote")
            }

            body {
                blockquote {
                    p { +"$quote" }
                    p {
                        entity("mdash")
                        +" "
                        cite { +"$author" }
                    }
                }
            }
        }.toXmlString()
    }

    @Get("/fib([n])", result = "text/plain")
    fun getFib(@PathParameter n: Long): Long {
        var a = 0L
        var b = 1L
        for (i in 0L until n) {
            val c = a + b
            a = b
            b = c
        }
        return a
    }

    @Get("/test", "text/html")
    fun getTest(): String {
        return Bundle().html {
            head {
                title("Hello world!")
            }

            body {
                h1 { +"Hello world!" }
                p { +"Lorem ipsum dolor sit amet" }

                button {
                    +"Click me!"

                    on("click") {
                        +console.log("Hello world from click listener!")
                    }
                }

                h2 { +"some split content" }

                div({ htmlClass = "split" }) {
                    section {
                        h3 { +"something on the left" }
                    }

                    section {
                        h3 { +"something on the right" }
                    }
                }

                style {
                    div("split") {
                        display = CssDisplay.FLEX
                        flexDirection = CssFlexDirection.ROW
                        flexWrap = CssFlexWrap.NOWRAP
                        alignItems = CssAlignItems.FLEX_START
                        justifyContent = CssJustifyContent.SPACE_BETWEEN
                    }
                }
            }
        }.toXmlString()
    }
}
