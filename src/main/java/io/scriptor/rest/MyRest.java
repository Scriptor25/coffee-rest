package io.scriptor.rest;

import io.scriptor.annotation.*;
import io.scriptor.http.HTTPMethod;
import io.scriptor.log.Log;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@Endpoint("/")
public class MyRest {

    @Resource(value = "hello", result = "text/plain")
    public String getHello() {
        return "Hello World!";
    }

    @Resource(value = "favicon.[ext]", result = "image/svg+xml")
    public InputStream getFavicon() {
        return ClassLoader.getSystemResourceAsStream("favicon.svg");
    }

    @Resource(value = "message/[from]/[to]", method = HTTPMethod.POST, accept = "text/plain", result = "text/plain")
    public void postMessage(
            final @Path("from") String from,
            final @Path("to") String to,
            final @Body InputStream body,
            final @Header("Content-Length") Integer contentLength
    ) throws IOException {
        final var bytes = body.readNBytes(contentLength);
        Log.info("message (from %s to %s): %s", from, to, new String(bytes));
    }

    @Resource(value = "lorem", method = HTTPMethod.GET, result = "application/json")
    public JSONObject getLorem(final @Query("count") Integer count) throws IOException {
        final var stream = Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("lorem.txt"));

        final var bytes = count == null ? stream.readAllBytes() : stream.readNBytes(count);
        final var lorem = new String(bytes);

        final var json = new JSONObject();
        json.put("count", count == null ? bytes.length : count);
        json.put("text", lorem);

        return json;
    }

    @Resource(value = "lorem-stream", method = HTTPMethod.GET, result = "text/plain")
    public InputStream getLoremStream() {
        return Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("lorem.txt"));
    }

    @Resource(value = "random-quote", method = HTTPMethod.GET, result = "application/json")
    public InputStream getRandomQuote() throws IOException {
        final var url        = new URL("https://dummyjson.com/quotes/random");
        final var connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection.getInputStream();
    }
}
