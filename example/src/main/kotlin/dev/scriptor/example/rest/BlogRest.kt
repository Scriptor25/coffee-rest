package dev.scriptor.example.rest

import dev.scriptor.server.annotation.Controller
import dev.scriptor.server.annotation.Get
import dev.scriptor.server.annotation.PathParameter
import org.json.JSONObject

@Controller("/blog")
class BlogRest {

    @Get("/", result = "text/html")
    fun getList(): String = """
        <!DOCTYPE html>
        <html>
            <head>
                <title>Blogs</title>
            </head>
            <body>
                <ul>
                    <li><a href="/blog/foo.md">Foo</a></li>
                    <li><a href="/blog/bar.md">Bar</a></li>
                    <li><a href="/blog/bar/foo.md">Bar Foo</a></li>
                    <li><a href="/blog/foo/bar.md">Foo Bar</a></li>
                </ul>
            </body>
        </html>
    """.trimIndent()

    @Get("/[slug+]", result = "text/html")
    fun getArticle(@PathParameter slug: Array<String>): String = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>${slug[slug.size - 1]} | Blogs</title>
        </head>
        <body>
            <h1>${slug[slug.size - 1]}</h1>
            ${slug.joinToString("", "<ul>", "</ul>") { "<li>${it}</li>" }}
        </body>
        </html>
    """.trimIndent()

    @Get("/[slug+]/metadata", result = "application/json")
    fun getArticleMetadata(@PathParameter slug: Array<String>): JSONObject {
        val json = JSONObject()
        json.put("title", slug[slug.size - 1])
        json.put("slug", slug)

        return json
    }

    @Get("/[slug+]/metadata/text", result = "text/plain")
    fun getArticleMetadataText(@PathParameter slug: Array<String>): String {
        return "title: ${slug[slug.size - 1]}, slug: ${slug.contentToString()}"
    }
}
