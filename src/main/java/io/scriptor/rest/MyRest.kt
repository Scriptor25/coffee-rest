package io.scriptor.rest

import io.scriptor.annotation.*
import io.scriptor.http.HTTPMethod
import io.scriptor.http.result.HTTPResultVoid
import io.scriptor.log.info
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI

@Endpoint("/my")
class MyRest {

    @Resource(path = "hello", result = "text/html")
    fun getHello(): InputStream? = ClassLoader.getSystemResourceAsStream("hello.html")

    @Resource(path = "message/[from]/[to]", method = HTTPMethod.POST, accept = "text/plain", result = "text/plain")
    fun postMessage(
        @Parameter("from") from: String,
        @Parameter("to") to: String,
        @Body body: InputStream,
        @Header("Content-Length") contentLength: Int
    ): HTTPResultVoid {
        val bytes = body.readNBytes(contentLength)
        info("message (from %s to %s): %s", from, to, String(bytes))
        return HTTPResultVoid(201, "Message Sent")
    }

    @Resource(path = "lorem", result = "application/json")
    fun getLorem(@Query("count") count: Int?): JSONObject? {
        val stream = ClassLoader.getSystemResourceAsStream("lorem.txt") ?: return null

        val bytes =
            if (count == null) stream.readAllBytes()
            else stream.readNBytes(count)
        val lorem = String(bytes)

        val json = JSONObject()
        json.put("count", count ?: bytes.size)
        json.put("text", lorem)

        return json
    }

    @Resource(path = "lorem-stream", result = "text/plain")
    fun getLoremStream(): InputStream? = ClassLoader.getSystemResourceAsStream("lorem.txt")

    @Resource(path = "random-quote", result = "text/html")
    fun getRandomQuote(): String {
        val url = URI("https://dummyjson.com/quotes/random").toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val text = connection.getInputStream().readAllBytes().decodeToString();
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
    fun getFib(@Parameter("n") n: Int): Int {
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
