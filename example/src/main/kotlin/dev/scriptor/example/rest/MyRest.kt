package dev.scriptor.example.rest

import dev.scriptor.server.NotFoundSignal
import dev.scriptor.server.annotation.*
import dev.scriptor.server.http.Method.POST
import dev.scriptor.server.result.UnitResult
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.util.logging.Logger

@Endpoint("/my")
class MyRest {

    @Resource("/hello", result = "text/html")
    fun getHello(): InputStream =
        ClassLoader.getSystemResourceAsStream("hello.html")
            ?: throw NotFoundSignal()

    @Resource(
        "/message/[from]/[to]",
        POST,
        "text/plain",
        "text/plain",
    )
    context(log: Logger)
    fun postMessage(
        @PathParameter from: String,
        @PathParameter to: String,
        @Header("content-length") contentLength: Int,
        @Body body: InputStream,
    ): UnitResult {
        val bytes = body.readNBytes(contentLength)
        val message = bytes.decodeToString()
        log.info("message (from $from to $to): $message")
        return UnitResult(201, "Message Sent")
    }

    @Resource("/random-quote", result = "text/html")
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

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>${author} | Random Quote</title>
            </head>
            <body>
                <blockquote>
                    <p>${quote}</p>
                    <p>&mdash; <cite>${author}</cite></p>
                </blockquote>
            </body>
            </html>
        """.trimIndent()
    }

    @Resource("/fib([n])", result = "text/plain")
    fun getFib(@PathParameter n: Int): Int {
        var a = 0
        var b = 1
        for (i in 0 until n) {
            val c = a + b
            a = b
            b = c
        }
        return a
    }
}
