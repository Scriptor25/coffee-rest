package io.scriptor.rest;

import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Parameter;
import io.scriptor.annotation.Resource;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Endpoint("/")
public class BlogRest {

    @Resource(path = "/", result = "text/html")
    public String getList() {
        return """
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
               """;
    }

    @Resource(path = "/blog/[slug+]", result = "text/html")
    public String getArticle(final @Parameter("slug") String[] slug) throws IOException {

        //        final var path = Path.of(slug[0], Arrays.copyOfRange(slug, 1, slug.length));
        //        return FileChannel.open(path);

        return """
               <!DOCTYPE html>
               <html>
               
               <head>
                 <title>%1$s | Blogs</title>
               </head>
               
               <body>
                 <h1>%1$s</h1>
                 <ul>%2$s</ul>
               </body>
               
               </html>
               """.formatted(slug[slug.length - 1],
                             Arrays.stream(slug).map("<li>%s</li>"::formatted).collect(Collectors.joining()));
    }

    @Resource(path = "/blog/[slug+]/metadata", result = "application/json")
    public JSONObject getArticleMetdata(final @Parameter("slug") String[] slug) throws IOException {

        final var json = new JSONObject();
        json.put("title", slug[slug.length - 1]);
        json.put("slug", slug);

        return json;
    }

    @Resource(path = "/blog/[slug+]/metadata/text", result = "text/plain")
    public String getArticleMetdataText(final @Parameter("slug") String[] slug) throws IOException {

        return "title: %s, slug: %s".formatted(slug[slug.length - 1], Arrays.toString(slug));
    }
}
