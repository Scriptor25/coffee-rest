package io.scriptor.rest

import io.scriptor.annotation.Endpoint
import io.scriptor.annotation.Parameter
import io.scriptor.annotation.Resource
import org.json.JSONObject
import java.util.*
import java.util.stream.Collectors

@Endpoint("/blog")
class BlogRest {

    @Resource(path = "/", result = "text/html")
    fun getList(): String = """
        <!DOCTYPE html>
        <html>
            <head>
                <title>Blogs</title>
            </head>
            <body>
                <ul>
                    <li><a href="./foo.md">Foo</a></li>
                    <li><a href="./bar.md">Bar</a></li>
                    <li><a href="./bar/foo.md">Bar Foo</a></li>
                    <li><a href="./foo/bar.md">Foo Bar</a></li>
                </ul>
            </body>
        </html>
    """.trimIndent()

    @Resource(path = "/[slug+]", result = "text/html")
    fun getArticle(@Parameter("slug") slug: Array<String>): String = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>${slug[slug.size - 1]} | Blogs</title>
        </head>
        <body>
            <h1>${slug[slug.size - 1]}</h1>
            <ul>${Arrays.stream(slug).map { "<li>${it}</li>" }.collect(Collectors.joining())}</ul>
        </body>
        </html>
    """.trimIndent()

    @Resource(path = "/[slug+]/metadata", result = "application/json")
    fun getArticleMetadata(@Parameter("slug") slug: Array<String>): JSONObject {
        val json = JSONObject()
        json.put("title", slug[slug.size - 1])
        json.put("slug", slug)

        return json
    }

    @Resource(path = "/[slug+]/metadata/text", result = "text/plain")
    fun getArticleMetadataText(@Parameter("slug") slug: Array<String>): String {
        return "title: ${slug[slug.size - 1]}, slug: ${slug.contentToString()}"
    }
}
