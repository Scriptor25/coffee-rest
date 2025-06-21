package io.scriptor.rest;

import io.scriptor.annotation.Endpoint;
import io.scriptor.annotation.Parameter;
import io.scriptor.annotation.Resource;
import io.scriptor.http.HTTPMethod;
import io.scriptor.log.Log;
import io.scriptor.result.StreamResult;
import io.scriptor.result.StringResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@Endpoint("/")
public class MyRest {

    @Resource(value = "hello", result = "text/plain")
    public StringResult getHello() {
        return new StringResult(200, "OK", "Hello World!");
    }

    @Resource(value = "favicon.[ext]", result = "image/svg+xml")
    public StreamResult getFavicon(final @Parameter("ext") String ext) {
        final var stream = Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("favicon.svg"));
        return new StreamResult(200, "OK", stream);
    }

    @Resource(value = "message/[from]/[to]", method = HTTPMethod.POST, accept = "text/plain", result = "text/plain")
    public StringResult postMessage(
            final @Parameter("from") String from,
            final @Parameter("to") String to,
            final @Parameter("body") InputStream body,
            final @Parameter("headers") Map<String, String> headers
    ) throws IOException {
        final var count = Integer.parseInt(headers.get("Content-Length"));
        final var bytes = body.readNBytes(count);
        Log.info("message (from %s to %s): %s", from, to, new String(bytes));
        return new StringResult(201, "Created", "Sent message");
    }
}
