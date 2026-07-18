package dev.scriptor.example.rest

import dev.scriptor.server.annotation.*
import dev.scriptor.server.http.result.HTTPResultVoid
import dev.scriptor.server.log.info
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI

@Endpoint("/my")
class MyRest {

    @Resource(path = "hello", result = "text/html")
    fun getHello(): InputStream? = ClassLoader.getSystemResourceAsStream("hello.html")

    @Resource(
        path = "message/[from]/[to]",
        method = dev.scriptor.server.http.HTTPMethod.POST,
        accept = "text/plain",
        result = "text/plain"
    )
    fun postMessage(
        @Path("from") from: String,
        @Path("to") to: String,
        @Body body: InputStream,
        @Header("Content-Length") contentLength: Int
    ): HTTPResultVoid {
        val bytes = body.readNBytes(contentLength)
        info("message (from %s to %s): %s", from, to, String(bytes))
        return HTTPResultVoid(201, "Message Sent")
    }

    @Resource(path = "random-quote", result = "text/html")
    fun getRandomQuote(): String {
        val url = URI("https://dummyjson.com/quotes/random").toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val text = connection.getInputStream().readAllBytes().decodeToString()
        val json = JSONObject(text)

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

    @Resource(path = "fib([n])", result = "text/plain")
    fun getFib(@Path("n") n: Int): Int {
        var a = 0
        var b = 1
        for (i in 0..<n) {
            val c = a + b
            a = b
            b = c
        }
        return a
    }
}
