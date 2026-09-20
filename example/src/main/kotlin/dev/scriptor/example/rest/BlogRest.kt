package dev.scriptor.example.rest

import dev.scriptor.server.jvm.annotation.Controller
import dev.scriptor.server.jvm.annotation.Get
import dev.scriptor.server.jvm.annotation.PathParameter
import dev.scriptor.ui.bundle
import org.json.JSONObject

@Controller("/blog")
class BlogRest {

    @Get("/", result = "text/html")
    fun getList(): String = bundle {
        html {
            head {
                meta(charset = "utf-8")
                title("Blogs")
            }

            body {
                h1 { +"Blogs" }
                ul {
                    li {
                        a({ href = "/blog/foo.md" }) { +"Foo" }
                    }
                    li {
                        a({ href = "/blog/bar.md" }) { +"Bar" }
                    }
                    li {
                        a({ href = "/blog/bar/foo.md" }) { +"Bar Foo" }
                    }
                    li {
                        a({ href = "/blog/foo/bar.md" }) { +"Foo Bar" }
                    }
                }
            }
        }
    }.toString()

    @Get("/[slug+]", result = "text/html")
    fun getArticle(@PathParameter slug: Array<String>): String = bundle {
        html {
            head {
                meta(charset = "utf-8")
                title("${slug.last()} | Blogs")
            }

            body {
                h1 { +slug.last() }
                ul {
                    for (segment in slug) {
                        li { +segment }
                    }
                }
            }
        }
    }.toString()


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
