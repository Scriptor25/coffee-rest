package io.scriptor.rest;

import io.scriptor.annotation.*;
import io.scriptor.http.HTTPMethod;
import io.scriptor.http.HTTPResultVoid;
import io.scriptor.log.Log;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Endpoint("/")
public class MyRest {

    @Resource(path = "hello", method = HTTPMethod.GET, result = "text/html")
    public String getHello() {
        return "<html><head><title>Hello</title></head><body><h1>Hello World!</h1></body></html>";
    }

    @Resource(path = "favicon.[]", method = HTTPMethod.GET, result = "image/svg+xml")
    public InputStream getFavicon() {
        return ClassLoader.getSystemResourceAsStream("favicon.svg");
    }

    @Resource(path = "message/[from]/[to]", method = HTTPMethod.POST, accept = "text/plain", result = "text/plain")
    public HTTPResultVoid postMessage(
            final @Path("from") String from,
            final @Path("to") String to,
            final @Body InputStream body,
            final @Header("Content-Length") Integer contentLength
    ) throws IOException {
        final var bytes = body.readNBytes(contentLength);
        Log.info("message (from %s to %s): %s", from, to, new String(bytes));
        return new HTTPResultVoid(201, "Message Sent");
    }

    @Resource(path = "lorem", method = HTTPMethod.GET, result = "application/json")
    public JSONObject getLorem(final @Query("count") Integer count) throws IOException {
        final var stream = Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("lorem.txt"));

        final var bytes = count == null ? stream.readAllBytes() : stream.readNBytes(count);
        final var lorem = new String(bytes);

        final var json = new JSONObject();
        json.put("count", count == null ? bytes.length : count);
        json.put("text", lorem);

        return json;
    }

    @Resource(path = "lorem-stream", method = HTTPMethod.GET, result = "text/plain")
    public InputStream getLoremStream() {
        return Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("lorem.txt"));
    }

    @Resource(path = "random-quote", method = HTTPMethod.GET, result = "application/json")
    public InputStream getRandomQuote() throws IOException, URISyntaxException {
        final var url        = new URI("https://dummyjson.com/quotes/random").toURL();
        final var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection.getInputStream();
    }
}
